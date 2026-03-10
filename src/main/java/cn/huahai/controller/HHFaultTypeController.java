package cn.huahai.controller;

import cn.huahai.entity.HHFaultType;
import cn.huahai.service.HHFaultTypeService;
import cn.huahai.common.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 故障类型 控制器（适配字符串主键）
 */
@RestController
@RequestMapping("/api/fault-type")
public class HHFaultTypeController {

    @Autowired
    private HHFaultTypeService faultTypeService;

    /**
     * 新增（仍需登录，后台管理用）
     */
    @PostMapping("/add")
    public Result add(@RequestBody HHFaultType faultType) {
        boolean save = faultTypeService.save(faultType);
        return save ? Result.success("新增成功") : Result.error("新增失败");
    }

    /**
     * 根据ID删除（改为String类型，仍需登录）
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) { // 核心修改：Long → String
        boolean remove = faultTypeService.removeById(id);
        return remove ? Result.success("删除成功") : Result.error("删除失败");
    }

    /**
     * 修改（仍需登录）
     */
    @PutMapping("/update")
    public Result update(@RequestBody HHFaultType faultType) {
        boolean update = faultTypeService.updateById(faultType);
        return update ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 根据ID查询（改为String类型，仍需登录）
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable String id) { // 核心修改：Long → String
        HHFaultType faultType = faultTypeService.getById(id);
        return Result.success(faultType);
    }

    /**
     * 查询所有（仅启用状态）✅ 无登录/权限注解，免登录访问（前端反馈页面用）
     */
    @GetMapping("/list")
    public Result list() {
        List<HHFaultType> list = faultTypeService.list(new QueryWrapper<HHFaultType>().eq("status", "enable"));
        return Result.success(list);
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHFaultType;
//import cn.huahai.service.HHFaultTypeService;
//import cn.huahai.common.Result;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 故障类型 控制器（适配字符串主键）
// */
//@RestController
//@RequestMapping("/api/fault-type")
//public class HHFaultTypeController {
//
//    @Autowired
//    private HHFaultTypeService faultTypeService;
//
//    /**
//     * 新增
//     */
//    @PostMapping("/add")
//    public Result add(@RequestBody HHFaultType faultType) {
//        boolean save = faultTypeService.save(faultType);
//        return save ? Result.success("新增成功") : Result.error("新增失败");
//    }
//
//    /**
//     * 根据ID删除（改为String类型）
//     */
//    @DeleteMapping("/delete/{id}")
//    public Result delete(@PathVariable String id) { // 核心修改：Long → String
//        boolean remove = faultTypeService.removeById(id);
//        return remove ? Result.success("删除成功") : Result.error("删除失败");
//    }
//
//    /**
//     * 修改
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody HHFaultType faultType) {
//        boolean update = faultTypeService.updateById(faultType);
//        return update ? Result.success("修改成功") : Result.error("修改失败");
//    }
//
//    /**
//     * 根据ID查询（改为String类型）
//     */
//    @GetMapping("/get/{id}")
//    public Result getById(@PathVariable String id) { // 核心修改：Long → String
//        HHFaultType faultType = faultTypeService.getById(id);
//        return Result.success(faultType);
//    }
//
//    /**
//     * 查询所有（仅启用状态）
//     */
//    @GetMapping("/list")
//    public Result list() {
//        List<HHFaultType> list = faultTypeService.list(new QueryWrapper<HHFaultType>().eq("status", "enable"));
//        return Result.success(list);
//    }
//}