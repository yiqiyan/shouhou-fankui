package cn.huahai.vo;

import cn.huahai.entity.HHFeedbackImage;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HHServiceFeedbackVO {
    // 主表字段（和HHServiceFeedback完全对齐，类型一致）
    private Long feedbackId;
    private String feedbackNo;
    private String contactName;
    private String contactPhone;
    private String identityCode;
    private String regionCode;
    private String detailAddress;
    private String fullAddress;
    private String productName;
    private String productModel;
    private String materialCode; // 补充：实体类有的字段VO也要有
    private String machineCode;  // 补充：实体类有的字段VO也要有

    // 关键修复：类型改为LocalDate（和实体类一致）
    private LocalDate purchaseDate;

    private String storeName;
    private String channelCode;
    private String contactTime;
    private String faultCode;
    private String faultDesc;
    private String supplementDesc;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String detailedAddress; // 补充：和实体类字段名对齐

    // 新增：图片列表字段
    private List<HHFeedbackImage> imageList;
}