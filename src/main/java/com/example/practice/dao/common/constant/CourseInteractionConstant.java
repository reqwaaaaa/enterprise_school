package com.example.practice.dao.common.constant;

/**
 * 课程互动类型常量
 * 与 t_course_interaction 表的 type 字段对应
 */
public interface CourseInteractionConstant {

    String TYPE_LIKE = "like";         // 点赞

    String TYPE_COMMENT = "comment";   // 评论

    String TYPE_COLLECT = "collect";   // 收藏

    String TYPE_COIN = "coin";         // 投币（可选，激励机制）

    String GROUP_COURSE_INTERACTIONS = "CourseInteractionsGroup";  // Redis/WebSocket 组名
}