package cn.huahai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 提交售后反馈的请求 DTO
 */
@Data
public class FeedbackSubmitDTO {

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系身份编码不能为空")
    private String identityCode; // 对应 HH_contact_identity.identity_code

    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    @NotNull(message = "所在地区ID不能为空")
    private Integer regionId; // 对应 HH_region.region_id

    private String detailedAddress;

    @NotBlank(message = "产品名称不能为空")
    private String productName;

    private String productModel;
    private String materialCode;

    @NotNull(message = "购买日期不能为空")
    private LocalDate purchaseDate;

    private String channelCode;   // HH_purchase_channel.channel_code
    private String storeName;
    private String faultCode;     // HH_fault_type.fault_code

    @NotBlank(message = "故障描述不能为空")
    private String faultDesc;

    private String supplementDesc;

    // 图片 URL 列表（前端上传后返回的 OSS/本地路径）
    private List<String> imageUrls;
}