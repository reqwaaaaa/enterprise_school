package com.example.practice.service.util;

import com.alibaba.fastjson.JSONArray;
import com.example.practice.entity.TCourse;
import com.example.practice.entity.TCourseInteraction;  // 修改为你的互动实体
import com.example.practice.service.TCourseInteractionService;
import com.example.practice.service.TCourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTasks {

  @Autowired
  private TCourseInteractionService tCourseInteractionService;  // 修改为你的互动服务

  @Autowired
  private TCourseService tCourseService;  // 修改为你的课程服务

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  // 每 60 秒执行一次，从 Redis 同步课程互动到数据库（防数据丢失）
  @Scheduled(fixedRate = 60000)
  public void syncCourseInteractions() {
    List<String> courseIds = getAllCourseIds();
    for (String courseId : courseIds) {
      String key = "course-interaction:" + courseId;  // 修改 Redis key 为你的项目风格
      String value = redisTemplate.opsForValue().get(key);
      if (StringUtils.isNotBlank(value)) {
        // 解析 value 为 TCourseInteraction 列表
        List<TCourseInteraction> interactionList = JSONArray.parseArray(value, TCourseInteraction.class);
        for (TCourseInteraction interaction : interactionList) {
          saveInteractionToDatabase(interaction);
        }
      }
      redisTemplate.delete(key);
      redisTemplate.delete("now-interaction-course-" + courseId);
    }
  }

  private List<String> getAllCourseIds() {
    List<String> list = new ArrayList<>();
    list = tCourseService.getAllCourseIds();  // 假设你的 TCourseService 有此方法，查询所有课程 ID
    return list;
  }

  private void saveInteractionToDatabase(TCourseInteraction interaction) {
    tCourseInteractionService.save(interaction);
  }
}