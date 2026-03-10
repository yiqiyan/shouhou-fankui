package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 联系身份枚举表实体类（对应HH_contact_identity表）
 */
@Data
@TableName("HH_contact_identity")
public class HHContactIdentity {

    /**
     * 身份编码（主键，唯一）
     */
    @TableId(value = "identity_code", type = IdType.INPUT)
    private String identityCode;

    /**
     * 身份名称（如：产品购买人员、产品使用人员）
     */
    private String identityName;

    /**
     * 排序号（控制前端下拉框顺序）
     */
    private Integer sort;

    /**
     * 状态：enable-启用/disable-禁用
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}