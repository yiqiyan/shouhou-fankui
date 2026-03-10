package cn.huahai.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 反馈图片信息（用于响应）
 */
@Data
public class ImageInfoDTO {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime uploadTime;
}