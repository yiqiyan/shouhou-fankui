package cn.huahai.controller;

import cn.huahai.entity.HHSysRole;
import cn.huahai.service.HHSysRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统角色Controller（SpringBoot3.1 + RESTful规范）
 */
@RestController
@RequestMapping("/api/system/role")
public class HHSysRoleController {

    @Resource
    private HHSysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public ResponseEntity<IPage<HHSysRole>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<HHSysRole> page = new Page<>(pageNum, pageSize);
        IPage<HHSysRole> rolePage = roleService.page(page);
        return ResponseEntity.ok(rolePage);
    }

    /**
     * 查询所有启用的角色
     */
    @GetMapping("/all")
    public ResponseEntity<List<HHSysRole>> getAllRoles() {
        List<HHSysRole> roles = roleService.selectAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * 根据角色ID查询角色
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<HHSysRole> getRoleById(@PathVariable Integer roleId) {
        HHSysRole role = roleService.selectRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    public ResponseEntity<Boolean> addRole(@RequestBody HHSysRole role) {
        boolean success = roleService.insertRole(role);
        return ResponseEntity.ok(success);
    }

    /**
     * 修改角色
     */
    @PutMapping
    public ResponseEntity<Boolean> updateRole(@RequestBody HHSysRole role) {
        boolean success = roleService.updateRole(role);
        return ResponseEntity.ok(success);
    }

    /**
     * 删除角色（逻辑删除）
     */
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Integer roleId) {
        boolean success = roleService.deleteRoleById(roleId);
        return ResponseEntity.ok(success);
    }

    /**
     * 查询角色关联的菜单ID
     */
    @GetMapping("/menu/{roleId}")
    public ResponseEntity<List<Integer>> getMenuIdsByRoleId(@PathVariable Integer roleId) {
        // 空值校验
        if (roleId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Integer> menuIds = roleService.selectMenuIdsByRoleId(roleId);
        return ResponseEntity.ok(menuIds);
    }

    /**
     * 为角色分配菜单
     */
    @PostMapping("/assignMenu")
    public ResponseEntity<Boolean> assignMenu(@RequestBody Map<String, Object> params) {
        // 空值校验
        Integer roleId = (Integer) params.get("roleId");
        List<Integer> menuIds = (List<Integer>) params.get("menuIds");
        if (roleId == null) {
            return ResponseEntity.badRequest().body(false);
        }
        // 菜单ID为空则清空该角色的所有菜单
        boolean success = roleService.assignMenu(roleId, menuIds == null ? List.of() : menuIds);
        return ResponseEntity.ok(success);
    }
}

//package cn.huahai.controller;
//
//import cn.huahai.entity.HHSysRole;
//import cn.huahai.service.HHSysRoleService;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import jakarta.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
///**
// * 系统角色Controller（SpringBoot3.1 + RESTful规范）
// */
//@RestController
//@RequestMapping("/api/system/role")
//public class HHSysRoleController {
//
//    @Resource
//    private HHSysRoleService roleService;
//
//    /**
//     * 分页查询角色列表
//     */
//    @GetMapping("/list")
//    public ResponseEntity<IPage<HHSysRole>> list(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "10") Integer pageSize) {
//        Page<HHSysRole> page = new Page<>(pageNum, pageSize);
//        IPage<HHSysRole> rolePage = roleService.page(page);
//        return ResponseEntity.ok(rolePage);
//    }
//
//    /**
//     * 查询所有启用的角色
//     */
//    @GetMapping("/all")
//    public ResponseEntity<List<HHSysRole>> getAllRoles() {
//        List<HHSysRole> roles = roleService.selectAllRoles();
//        return ResponseEntity.ok(roles);
//    }
//
//    /**
//     * 根据角色ID查询角色
//     */
//    @GetMapping("/{roleId}")
//    public ResponseEntity<HHSysRole> getRoleById(@PathVariable Integer roleId) {
//        HHSysRole role = roleService.selectRoleById(roleId);
//        return ResponseEntity.ok(role);
//    }
//
//    /**
//     * 新增角色
//     */
//    @PostMapping
//    public ResponseEntity<Boolean> addRole(@RequestBody HHSysRole role) {
//        boolean success = roleService.insertRole(role);
//        return ResponseEntity.ok(success);
//    }
//
//    /**
//     * 修改角色
//     */
//    @PutMapping
//    public ResponseEntity<Boolean> updateRole(@RequestBody HHSysRole role) {
//        boolean success = roleService.updateRole(role);
//        return ResponseEntity.ok(success);
//    }
//
//    /**
//     * 删除角色（逻辑删除）
//     */
//    @DeleteMapping("/{roleId}")
//    public ResponseEntity<Boolean> deleteRole(@PathVariable Integer roleId) {
//        boolean success = roleService.deleteRoleById(roleId);
//        return ResponseEntity.ok(success);
//    }
//
//    /**
//     * 查询角色关联的菜单ID
//     */
//    @GetMapping("/menu/{roleId}")
//    public ResponseEntity<List<Integer>> getMenuIdsByRoleId(@PathVariable Integer roleId) {
//        List<Integer> menuIds = roleService.selectMenuIdsByRoleId(roleId);
//        return ResponseEntity.ok(menuIds);
//    }
//
//    /**
//     * 为角色分配菜单
//     */
//    @PostMapping("/assignMenu")
//    public ResponseEntity<Boolean> assignMenu(@RequestBody Map<String, Object> params) {
//        Integer roleId = (Integer) params.get("roleId");
//        List<Integer> menuIds = (List<Integer>) params.get("menuIds");
//        boolean success = roleService.assignMenu(roleId, menuIds);
//        return ResponseEntity.ok(success);
//    }
//}