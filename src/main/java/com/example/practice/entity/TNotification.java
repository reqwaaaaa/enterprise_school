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
 * 消息通知表 - 消息推送
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("t_notification")
public class TNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收用户ID
     */
    @TableField("user_id")
    private Long userId;

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
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 是否已读: 0未读, 1已读
     */
    @TableField("is_read")
    private Byte isRead;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
