package cn.huahai.controller;

import cn.huahai.entity.HHSysMenu;
import cn.huahai.service.HHSysMenuService;
import cn.huahai.service.HHSysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单管理控制器
 * 核心：菜单CRUD + 树形菜单查询 + 角色菜单分配
 */
@Slf4j
@RestController
@RequestMapping("/api/system/menu")
public class HHSysMenuController {

    @Resource
    private HHSysMenuService hhSysMenuService;

    // 新增：注入角色Service（适配你的assignMenu方法）
    @Resource
    private HHSysRoleService hhSysRoleService;

    /**
     * 1. 查询所有菜单（树形结构）- 用于菜单管理页面展示
     */
    @GetMapping("/tree")
    public List<HHSysMenu> getAllMenuTree() {
        try {
            return hhSysMenuService.getAllMenuTree();
        } catch (Exception e) {
            log.error("查询树形菜单失败", e);
            return null;
        }
    }

    /**
     * 2. 根据角色编码查询菜单（树形）- 用于角色分配菜单
     */
    @GetMapping("/role/{roleCode}")
    public List<HHSysMenu> getMenuTreeByRoleCode(@PathVariable String roleCode) {
        if (roleCode == null || roleCode.trim().isEmpty()) {
            return null;
        }
        try {
            return hhSysMenuService.getMenuTreeByRoleCode(roleCode);
        } catch (Exception e) {
            log.error("根据角色编码查询菜单失败，roleCode：{}", roleCode, e);
            return null;
        }
    }

    /**
     * 3. 查询所有平级菜单 - 用于新增/编辑菜单时选择父菜单
     */
    @GetMapping("/list")
    public List<HHSysMenu> getAllMenuList() {
        try {
            return hhSysMenuService.getAllMenuList();
        } catch (Exception e) {
            log.error("查询平级菜单失败", e);
            return null;
        }
    }

    /**
     * 4. 新增菜单
     */
    @PostMapping
    public Boolean addMenu(@RequestBody HHSysMenu menu) {
        if (menu == null || menu.getMenuName() == null || menu.getMenuName().trim().isEmpty()) {
            return false;
        }
        try {
            boolean success = hhSysMenuService.save(menu);
            log.info("新增菜单{}，结果：{}", menu.getMenuName(), success);
            return success;
        } catch (Exception e) {
            log.error("新增菜单失败", e);
            return false;
        }
    }

    /**
     * 5. 编辑菜单
     */
    @PutMapping
    public Boolean updateMenu(@RequestBody HHSysMenu menu) {
        if (menu == null || menu.getMenuId() == null) {
            return false;
        }
        try {
            boolean success = hhSysMenuService.updateById(menu);
            log.info("编辑菜单ID{}，结果：{}", menu.getMenuId(), success);
            return success;
        } catch (Exception e) {
            log.error("编辑菜单失败", e);
            return false;
        }
    }

    /**
     * 6. 删除菜单（级联删除子菜单）
     */
    @DeleteMapping("/{menuId}")
    public Boolean deleteMenu(@PathVariable Integer menuId) {
        if (menuId == null) {
            return false;
        }
        try {
            boolean success = hhSysMenuService.removeById(menuId);
            log.info("删除菜单ID{}，结果：{}", menuId, success);
            return success;
        } catch (Exception e) {
            log.error("删除菜单失败", e);
            return false;
        }
    }

    /**
     * 7. 根据ID查询单个菜单 - 用于编辑回显
     */
    @GetMapping("/{menuId}")
    public HHSysMenu getMenuById(@PathVariable Integer menuId) {
        if (menuId == null) {
            return null;
        }
        try {
            return hhSysMenuService.getById(menuId);
        } catch (Exception e) {
            log.error("查询菜单详情失败，menuId：{}", menuId, e);
            return null;
        }
    }

    /**
     * 8. 角色分配菜单（适配你的assignMenu方法）
     */
    @PostMapping("/assignRoleMenu")
    public Map<String, Object> assignRoleMenu(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 解析角色ID（适配你的Service接收roleId）
            Integer roleId = null;
            Object roleIdObj = params.get("roleId");
            if (roleIdObj instanceof Number) {
                roleId = ((Number) roleIdObj).intValue();
            } else if (roleIdObj instanceof String) {
                roleId = Integer.parseInt((String) roleIdObj);
            }

            // 解析菜单ID列表
            List<Integer> menuIds = null;
            if (params.containsKey("menuIds") && params.get("menuIds") instanceof List<?>) {
                List<?> rawMenuIds = (List<?>) params.get("menuIds");
                menuIds = rawMenuIds.stream()
                        .filter(id -> id != null)
                        .map(id -> {
                            if (id instanceof Number) return ((Number) id).intValue();
                            else if (id instanceof String) return Integer.parseInt((String) id);
                            return null;
                        })
                        .filter(id -> id != null)
                        .toList();
            }

            // 参数校验
            if (roleId == null || menuIds == null || menuIds.isEmpty()) {
                result.put("code", 400);
                result.put("msg", "角色ID或菜单ID不能为空");
                return result;
            }

            // 调用你已实现的assignMenu方法
            boolean success = hhSysRoleService.assignMenu(roleId, menuIds);
            if (success) {
                log.info("角色{}分配菜单{}成功", roleId, menuIds);
                result.put("code", 200);
                result.put("msg", "角色分配菜单成功");
            } else {
                result.put("code", 500);
                result.put("msg", "角色分配菜单失败");
            }
            return result;
        } catch (Exception e) {
            log.error("角色分配菜单失败", e);
            result.put("code", 500);
            result.put("msg", "服务器错误，分配菜单失败");
            return result;
        }
    }
}