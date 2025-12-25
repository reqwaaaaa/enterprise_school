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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 成绩表 - 成绩管理
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@TableName("t_score")
@Accessors(chain = true)
public class TScore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 考试ID
     */
    @TableField("exam_id")
    private Long examId;

    /**
     * 分数
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 通过状态
     */
    @TableField("pass_status")
    private String passStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
