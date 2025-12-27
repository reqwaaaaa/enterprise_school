package com.example.practice.service;

import com.example.practice.entity.TCourseInteraction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程互动表 - 点赞/评论/收藏 服务类
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
public interface TCourseInteractionService extends IService<TCourseInteraction> {
    void asyncAddInteraction(TCourseInteraction interaction);
    void addToRedis(TCourseInteraction interaction);
}
