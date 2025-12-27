package com.example.practice.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.practice.entity.TCourseInteraction;
import com.example.practice.mapper.TCourseInteractionMapper;
import com.example.practice.service.TCourseInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程互动表 - 点赞/评论/收藏 服务实现类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Service
public class TCourseInteractionServiceImpl extends ServiceImpl<TCourseInteractionMapper, TCourseInteraction>
        implements TCourseInteractionService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 异步保存互动到数据库（削峰）
     */
    @Async  // 异步执行，需要启动类加 @EnableAsync
    @Override
    public void asyncAddInteraction(TCourseInteraction interaction) {
        this.save(interaction);  // 直接保存
    }

    /**
     * 将互动添加到 Redis（用于实时显示）
     */
    @Override
    public void addToRedis(TCourseInteraction interaction) {
        String key = "course-interaction:" + interaction.getCourseId();
        String value = redisTemplate.opsForValue().get(key);

        List<TCourseInteraction> list = new ArrayList<>();
        if (value != null) {
            list = JSONArray.parseArray(value, TCourseInteraction.class);
        }
        list.add(interaction);

        redisTemplate.opsForValue().set(key, JSONArray.toJSONString(list));
    }
}
