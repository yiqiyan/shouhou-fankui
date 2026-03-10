package cn.huahai.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 售后反馈详情响应 DTO
 */
@Data
public class FeedbackDetailDTO {

    private Long feedbackId;
    private String contactName;
    private String identityCode;      // 可考虑扩展为 IdentityDTO（含名称）
    private String contactPhone;
    private Integer regionId;         // 可考虑扩展为 RegionDTO
    private String detailedAddress;
    private String productName;
    private String productModel;
    private String materialCode;
    private LocalDate purchaseDate;
    private String channelCode;
    private String storeName;
    private String faultCode;         // 可考虑扩展为 FaultTypeDTO
    private String faultDesc;
    private String supplementDesc;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联的图片列表
    private List<ImageInfoDTO> images;
}