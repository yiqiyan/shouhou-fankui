package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("HH_fault_type")
public class HHFaultType {

    /**
     * 故障类型编码（主键）
     */
    @TableId(value = "fault_code", type = IdType.INPUT) // INPUT表示手动输入/自动填充
    private String faultCode; // 去掉默认值，交给填充处理器

    /**
     * 故障类型名称
     */
    private String faultName;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态：enable-启用 / disable-禁用
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 新增时自动填充创建时间
    private LocalDateTime createTime;
}