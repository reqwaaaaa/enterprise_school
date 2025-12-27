package com.example.practice.service.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.example.practice.entity.TCourseInteraction;
import com.example.practice.service.TCourseInteractionService;
import com.example.practice.service.util.RocketMQUtil;
import com.example.practice.service.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/imserver/{token}")
public class WebSocketService {

    private static ApplicationContext applicationContext;

    // 解决单例注入问题
    public static void setApplicationContext(ApplicationContext context) {
        WebSocketService.applicationContext = context;
    }

    private final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    private Long userId;
    private Session session;
    private String sessionId;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            this.userId = TokenUtil.verifyToken(token);
        } catch (Exception ignored) {
            this.userId = null;  // token 无效
        }
        this.sessionId = session.getId();
        this.session = session;

        WEBSOCKET_MAP.put(sessionId, this);
        ONLINE_COUNT.incrementAndGet();

        logger.info("用户连接成功：{}，当前在线人数：{}", sessionId, ONLINE_COUNT.get());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("连接异常", e);
        }
    }

    @OnClose
    public void onClose() {
        if (StringUtils.isNotBlank(sessionId) && WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.decrementAndGet();
        }
        logger.info("用户退出：{}，当前在线人数：{}", sessionId, ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("收到消息 from {}: {}", sessionId, message);
        if (StringUtils.isBlank(message)) return;

        try {
            // 群发消息（用 MQ 广播）
            DefaultMQProducer producer = applicationContext.getBean("notificationProducer", DefaultMQProducer.class);
            JSONObject json = new JSONObject();
            json.put("message", message);
            json.put("fromSessionId", sessionId);
            Message msg = new Message("course-interaction-topic", json.toJSONString().getBytes("UTF-8"));
            RocketMQUtil.asyncSendMsg(producer, msg);

            // 保存互动（如果登录了）
            if (userId != null) {
                TCourseInteraction interaction = JSONObject.parseObject(message, TCourseInteraction.class);
                interaction.setUserId(userId);
                TCourseInteractionService service = applicationContext.getBean(TCourseInteractionService.class);
                service.asyncAddInteraction(interaction);  // 异步保存数据库
                service.addToRedis(interaction);           // 保存到 Redis
            }
        } catch (Exception e) {
            logger.error("消息处理异常", e);
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        logger.error("WebSocket 错误", throwable);
    }

    public void sendMessage(String message) throws IOException {
        if (session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }

    @Scheduled(fixedRate = 5000)
    private void noticeOnlineCount() {
        JSONObject json = new JSONObject();
        json.put("onlineCount", ONLINE_COUNT.get());
        json.put("message", "当前在线人数为：" + ONLINE_COUNT.get());

        WEBSOCKET_MAP.forEach((id, ws) -> {
            try {
                if (ws.session.isOpen()) {
                    ws.sendMessage(json.toJSONString());
                }
            } catch (IOException e) {
                logger.error("推送在线人数失败", e);
            }
        });
    }

    public Session getSession() {
        return session;
    }
}