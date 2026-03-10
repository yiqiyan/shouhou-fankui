//package cn.huahai.entity;
//
//import com.baomidou.mybatisplus.annotation.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///**
// * 服务反馈实体类（精准对齐数据库表结构）
// */
//@Data
//@TableName("hh_service_feedback") // 严格匹配数据库表名（小写）
//public class HHServiceFeedback {
//
//    /**
//     * 反馈记录唯一ID（主键，自增）
//     */
//    @TableId(type = IdType.AUTO)
//    private Long feedbackId;
//
//    /**
//     * 联系人姓名（必填）
//     */
//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private String contactName;
//
//    /**
//     * 联系身份编码（必填）
//     */
//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private String identityCode;
//
//    /**
//     * 联系电话（必填）
//     */
//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private String contactPhone;
//
//    /**
//     * 详细地址（可选）
//     */
//    private String detailedAddress;
//
//    /**
//     * 产品名称（可选）
//     */
//    private String productName;
//
//    /**
//     * 产品型号（可选）
//     */
//    private String productModel;
//
//    /**
//     * 物控编码（可选）
//     */
//    private String materialCode;
//
//    /**
//     * 购买日期（可选）
//     */
//    private LocalDate purchaseDate;
//
//    /**
//     * 购买渠道编码（可选）
//     */
//    private String channelCode;
//
//    /**
//     * 店铺名/网点名（可选）
//     */
//    private String storeName;
//
//    /**
//     * 故障类型编码（可选）
//     */
//    private String faultCode;
//
//    /**
//     * 故障描述（可选，长文本）
//     */
//    private String faultDesc;
//
//    /**
//     * 补充说明（可选，长文本）
//     */
//    private String supplementDesc;
//
//    /**
//     * 处理状态（必填，字典：pending-待处理/processing-处理中/completed-已完成）
//     */
//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private String status;
//
//    /**
//     * 反馈提交时间（必填，默认当前时间）
//     */
//    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
//    private LocalDateTime createTime;
//
//    /**
//     * 最后修改时间（可选）
//     */
//    private LocalDateTime updateTime;
//
//    /**
//     * 最细层级地区编码（如区县编码，可选）
//     */
//    private String regionCode;
//
//    /**
//     * 详细地址（如XX街道XX小区XX号，可选）
//     */
//    private String detailAddress;
//
//    /**
//     * 完整地址（省+市+区+详细地址，可选）
//     */
//    private String fullAddress;
//
//    /**
//     * 业务反馈单号（如FB_20260227001，可选）
//     */
//    private String feedbackNo;
//}

package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 售后服务反馈实体类（修复逻辑删除+表名+字段映射问题）
 */
@Data
@TableName("hh_service_feedback") // 修复1：表名改为小写，和数据库一致
public class HHServiceFeedback {

    /**
     * 反馈ID（主键，自增）
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedbackId;

    /**
     * 联系人姓名
     */
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系身份编码（关联 HH_contact_identity.identity_code）
     */
    @NotBlank(message = "联系身份不能为空")
    private String identityCode;

    /**
     * 联系电话（简单格式校验）
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    @TableField("feedback_no")
    private String feedbackNo; // 业务单号

    /**
     * 详细地址
     */
    private String detailedAddress;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 购买日期
     */
    @NotNull(message = "购买日期不能为空")
    private LocalDate purchaseDate;

    /**
     * 购买渠道编码（关联 HH_purchase_channel.channel_code）
     */
    private String channelCode;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 故障类型编码（关联 HH_fault_type.fault_code）
     */
    private String faultCode;

    /**
     * 故障描述
     */
    @NotBlank(message = "故障描述不能为空")
    private String faultDesc;

    /**
     * 补充说明
     */
    private String supplementDesc;

    /**
     * 修复2：移除 @TableLogic 注解，status 是业务状态，不是逻辑删除字段
     * 状态：pending-待处理 / processing-处理中 / completed-已完成
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间（自动填充）
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    // 新增地址相关字段
    private String regionCode;       // 对应数据库的region_code
    private String detailAddress;    // 对应数据库的detail_address
    private String fullAddress;      // 对应数据库的full_address

    // 修复3：如果需要逻辑删除，新增专用字段（可选，根据业务需求）
    // @TableLogic
    // private Integer isDeleted; // 0-未删除，1-已删除

    private String machineCode; // 机器编码
}