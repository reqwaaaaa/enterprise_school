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
 * 频道表 - 网站群栏目管理
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@TableName("t_channel")
@Accessors(chain = true)
public class TChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 频道名称
     */
    @TableField("name")
    private String name;

    /**
     * 频道编码
     */
    @TableField("code")
    private String code;

    /**
     * 上级频道ID (树形结构)
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

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
