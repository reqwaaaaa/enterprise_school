package com.example.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 内容表 - 新闻、公告等内容管理
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@TableName("t_content")
@Accessors(chain = true)
public class TContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 类型: news新闻, announcement公告, policy政策, article文章
     */
    @TableField("type")
    private String type;

    /**
     * 作者ID
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 所属频道ID
     */
    @TableField("channel_id")
    private Long channelId;

    /**
     * 状态: draft草稿, published发布, archived归档
     */
    @TableField("status")
    private String status;

    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞量
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
