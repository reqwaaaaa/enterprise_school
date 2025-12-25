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
 * 招聘信息表 - 企业招聘发布
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@TableName("t_job")
@Accessors(chain = true)
public class TJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 职位标题
     */
    @TableField("title")
    private String title;

    /**
     * 职位描述
     */
    @TableField("description")
    private String description;

    /**
     * 企业ID
     */
    @TableField("enterprise_id")
    private Long enterpriseId;

    /**
     * 薪资范围
     */
    @TableField("salary")
    private String salary;

    /**
     * 工作地点
     */
    @TableField("location")
    private String location;

    /**
     * 任职要求
     */
    @TableField("requirements")
    private String requirements;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;

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
