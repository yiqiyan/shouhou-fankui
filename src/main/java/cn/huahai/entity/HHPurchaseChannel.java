package cn.huahai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("HH_purchase_channel")
public class HHPurchaseChannel {

    /**
     * 购买渠道编码（主键）
     */
    @TableId(value = "channel_code", type = IdType.INPUT)
    private String channelCode;

    /**
     * 购买渠道名称
     */
    private String channelName;

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
    private LocalDateTime createTime;
}