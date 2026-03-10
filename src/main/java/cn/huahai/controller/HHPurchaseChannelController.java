package cn.huahai.controller;

import cn.huahai.entity.HHPurchaseChannel;
import cn.huahai.service.HHPurchaseChannelService;
import cn.huahai.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购买渠道 控制器（适配字符串主键）
 */
@RestController
@RequestMapping("/api/purchase-channel")
public class HHPurchaseChannelController {

    @Autowired
    private HHPurchaseChannelService purchaseChannelService;

    /**
     * 新增（仍需登录，后台管理用）
     */
    @PostMapping("/add")
    public Result add(@RequestBody HHPurchaseChannel purchaseChannel) {
        boolean save = purchaseChannelService.save(purchaseChannel);
        return save ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 根据ID删除（改为String类型，仍需登录）
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) { // 核心修改：Long → String
        boolean remove = purchaseChannelService.removeById(id);
        return remove ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 修改（仍需登录）
     */
    @PutMapping("/update")
    public Result update(@RequestBody HHPurchaseChannel purchaseChannel) {
        boolean update = purchaseChannelService.updateById(purchaseChannel);
        return update ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 根据ID查询（改为String类型，仍需登录）
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable String id) { // 核心修改：Long → String
        HHPurchaseChannel purchaseChannel = purchaseChannelService.getById(id);
        return Result.success(purchaseChannel);
    }

    /**
     * 查询所有（仅启用状态）✅ 无登录/权限注解，免登录访问（前端反馈页面用）
     */
    @GetMapping("/list")
    public Result list() {
        List<HHPurchaseChannel> list = purchaseChannelService.list(new QueryWrapper<HHPurchaseChannel>().eq("status", "enable"));
        return Result.success(list);
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHPurchaseChannel;
//import cn.huahai.service.HHPurchaseChannelService;
//import cn.huahai.common.Result;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 购买渠道 控制器（适配字符串主键）
// */
//@RestController
//@RequestMapping("/api/purchase-channel")
//public class HHPurchaseChannelController {
//
//    @Autowired
//    private HHPurchaseChannelService purchaseChannelService;
//
//    /**
//     * 新增
//     */
//    @PostMapping("/add")
//    public Result add(@RequestBody HHPurchaseChannel purchaseChannel) {
//        boolean save = purchaseChannelService.save(purchaseChannel);
//        return save ? Result.success("新增成功") : Result.error("新增失败");
//    }
//
//    /**
//     * 根据ID删除（改为String类型）
//     */
//    @DeleteMapping("/delete/{id}")
//    public Result delete(@PathVariable String id) { // 核心修改：Long → String
//        boolean remove = purchaseChannelService.removeById(id);
//        return remove ? Result.success("删除成功") : Result.error("删除失败");
//    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody HHPurchaseChannel purchaseChannel) {
//        boolean update = purchaseChannelService.updateById(purchaseChannel);
//        return update ? Result.success("修改成功") : Result.error("修改失败");
//    }
//
//    /**
//     * 根据ID查询（改为String类型）
//     */
//    @GetMapping("/get/{id}")
//    public Result getById(@PathVariable String id) { // 核心修改：Long → String
//        HHPurchaseChannel purchaseChannel = purchaseChannelService.getById(id);
//        return Result.success(purchaseChannel);
//    }
//
//    /**
//     * 查询所有（仅启用状态）
//     */
//    @GetMapping("/list")
//    public Result list() {
//        List<HHPurchaseChannel> list = purchaseChannelService.list(new QueryWrapper<HHPurchaseChannel>().eq("status", "enable"));
//        return Result.success(list);
//    }
//}