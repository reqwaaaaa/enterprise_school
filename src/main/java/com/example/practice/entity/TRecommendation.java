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
 * 推荐表 - 课程/职位推荐
 * </p>
 *
 * @author Naiweilanlan
 * @since 2025-12-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("t_recommendation")
public class TRecommendation implements Serializable {

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
     * 推荐项ID (课程/职位/内容)
     */
    @TableField("recommended_item_id")
    private Long recommendedItemId;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 推荐分数
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
