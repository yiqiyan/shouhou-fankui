package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 反馈图片附件实体类（对应 HH_feedback_image 表）
 *
 * 说明：一张反馈记录（HH_service_feedback）可关联多张图片（一对多）
 */
@Data
@TableName("HH_feedback_image")
public class HHFeedbackImage {

    /**
     * 图片ID（主键，自增）
     */
    @TableId(value = "image_id", type = IdType.AUTO)
    private Long imageId;

    /**
     * 所属反馈ID（外键，关联 HH_service_feedback.feedback_id）
     */
    @NotNull(message = "反馈ID不能为空")
    private Long feedbackId;

    /**
     * 图片访问URL（存储路径或OSS地址）
     */
    private String imageUrl;

    /**
     * 上传时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充当前时间
    private LocalDateTime uploadTime;
}