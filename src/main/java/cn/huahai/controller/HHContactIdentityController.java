package cn.huahai.controller;

import cn.huahai.entity.HHContactIdentity;
import cn.huahai.service.HHContactIdentityService;
import cn.huahai.common.Result;
// 新增：导入LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict/contact-identity")
public class HHContactIdentityController {

    @Autowired
    private HHContactIdentityService contactIdentityService;

    // 新增接口：仍需登录（后台管理用）
    @PostMapping("/add")
    public Result add(@RequestBody HHContactIdentity contactIdentity) {
        boolean save = contactIdentityService.save(contactIdentity);
        return save ? Result.success("新增成功") : Result.error("新增失败");
    }

    // 删除接口：仍需登录（后台管理用）
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        boolean remove = contactIdentityService.removeById(id);
        return remove ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 修改接口：仍需登录（后台管理用）
    @PutMapping("/update")
    public Result update(@RequestBody HHContactIdentity contactIdentity) {
        boolean update = contactIdentityService.updateById(contactIdentity);
        return update ? Result.success("修改成功") : Result.error("修改失败");
    }

    // 单查接口：仍需登录（后台管理用）
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable String id) {
        HHContactIdentity contactIdentity = contactIdentityService.getById(id);
        return Result.success(contactIdentity);
    }

    // 列表接口：✅ 无任何登录/权限注解，天然支持免登录访问（前端反馈页面用）
    @GetMapping("/list")
    public Result list() {
        List<HHContactIdentity> list = contactIdentityService.list(
                new LambdaQueryWrapper<HHContactIdentity>()
                        .orderByAsc(HHContactIdentity::getSort)
        );
        return Result.success(list);
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHContactIdentity;
//import cn.huahai.service.HHContactIdentityService;
//import cn.huahai.common.Result;
//// 新增：导入LambdaQueryWrapper
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/dict/contact-identity")
//public class HHContactIdentityController {
//
//    @Autowired
//    private HHContactIdentityService contactIdentityService;
//
//    @PostMapping("/add")
//    public Result add(@RequestBody HHContactIdentity contactIdentity) {
//        boolean save = contactIdentityService.save(contactIdentity);
//        return save ? Result.success("新增成功") : Result.error("新增失败");
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public Result delete(@PathVariable String id) {
//        boolean remove = contactIdentityService.removeById(id);
//        return remove ? Result.success("删除成功") : Result.error("删除失败");
//    }
//
//    @PutMapping("/update")
//    public Result update(@RequestBody HHContactIdentity contactIdentity) {
//        boolean update = contactIdentityService.updateById(contactIdentity);
//        return update ? Result.success("修改成功") : Result.error("修改失败");
//    }
//
//    @GetMapping("/get/{id}")
//    public Result getById(@PathVariable String id) {
//        HHContactIdentity contactIdentity = contactIdentityService.getById(id);
//        return Result.success(contactIdentity);
//    }
//
//    // 修复后的list方法，无报错
//    @GetMapping("/list")
//    public Result list() {
//        List<HHContactIdentity> list = contactIdentityService.list(
//                new LambdaQueryWrapper<HHContactIdentity>()
//                        .orderByAsc(HHContactIdentity::getSort)
//        );
//        return Result.success(list);
//    }
//}