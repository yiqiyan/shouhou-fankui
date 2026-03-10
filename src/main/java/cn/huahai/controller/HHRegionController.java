package cn.huahai.controller;

import cn.huahai.entity.HHRegion;
import cn.huahai.service.HHRegionService;
import cn.huahai.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 地址表控制器（仅提供查询接口，无新增/修改/删除）
 */
@RestController
@RequestMapping("/api/region")
public class HHRegionController {

    @Autowired
    private HHRegionService regionService;

    /**
     * 查询所有省份 ✅ 无登录/权限注解，免登录访问（前端反馈页面用）
     */
    @GetMapping("/provinces")
    public Result<List<HHRegion>> listProvinces() {
        List<HHRegion> provinces = regionService.listProvinces();
        return Result.success(provinces);
    }

    /**
     * 根据父级编码查询下级地址（查市/区县）✅ 无登录/权限注解，免登录访问
     */
    @GetMapping("/children/{parentCode}")
    public Result<List<HHRegion>> listChildren(@PathVariable String parentCode) {
        List<HHRegion> children = regionService.listChildrenByParentCode(parentCode);
        return Result.success(children);
    }

    /**
     * 根据编码查询单个地址（用于回显/拼接完整地址）✅ 无登录/权限注解，免登录访问
     */
    @GetMapping("/{nodeCode}")
    public Result<HHRegion> getByCode(@PathVariable String nodeCode) {
        HHRegion region = regionService.getByNodeCode(nodeCode);
        return Result.success(region);
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHRegion;
//import cn.huahai.service.HHRegionService;
//import cn.huahai.common.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.util.List;
//
///**
// * 地址表控制器（仅提供查询接口，无新增/修改/删除）
// */
//@RestController
//@RequestMapping("/api/region")
//public class HHRegionController {
//
//    @Autowired
//    private HHRegionService regionService;
//
//    /**
//     * 查询所有省份
//     */
//    @GetMapping("/provinces")
//    public Result<List<HHRegion>> listProvinces() {
//        List<HHRegion> provinces = regionService.listProvinces();
//        return Result.success(provinces);
//    }
//
//    /**
//     * 根据父级编码查询下级地址（查市/区县）
//     */
//    @GetMapping("/children/{parentCode}")
//    public Result<List<HHRegion>> listChildren(@PathVariable String parentCode) {
//        List<HHRegion> children = regionService.listChildrenByParentCode(parentCode);
//        return Result.success(children);
//    }
//
//    /**
//     * 根据编码查询单个地址（用于回显/拼接完整地址）
//     */
//    @GetMapping("/{nodeCode}")
//    public Result<HHRegion> getByCode(@PathVariable String nodeCode) {
//        HHRegion region = regionService.getByNodeCode(nodeCode);
//        return Result.success(region);
//    }
//}