package cn.huahai.controller;

import cn.huahai.common.Result;
import cn.huahai.entity.HHFaultType;
import cn.huahai.entity.HHPurchaseChannel;
import cn.huahai.entity.HHRegion;
import cn.huahai.service.HHFaultTypeService;
import cn.huahai.service.HHPurchaseChannelService;
import cn.huahai.service.HHRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 枚举/字典数据控制器
 * 用于向前端提供下拉选单数据（故障类型、购买渠道、地区等）
 */
@RestController
@RequestMapping("/api/enum")
public class HHEnumController {

    @Autowired
    private HHFaultTypeService faultTypeService;

    @Autowired
    private HHPurchaseChannelService purchaseChannelService;

    @Autowired
    private HHRegionService regionService;

    /**
     * 获取所有故障类型列表
     */
    @GetMapping("/fault-type-list")
    public Result getFaultTypeList() {
        List<HHFaultType> list = faultTypeService.list();
        return Result.success(list);
    }

    /**
     * 获取所有购买渠道列表
     */
    @GetMapping("/purchase-channel-list")
    public Result getPurchaseChannelList() {
        List<HHPurchaseChannel> list = purchaseChannelService.list();
        return Result.success(list);
    }

    /**
     * 获取所有省份/地区列表
     */
    @GetMapping("/region-list")
    public Result getRegionList() {
        List<HHRegion> list = regionService.list();
        return Result.success(list);
    }
}