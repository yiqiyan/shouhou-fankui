package cn.huahai.service;

import cn.huahai.entity.HHSysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface HHSysMenuService extends IService<HHSysMenu> {
    // 根据角色编码查询树形菜单
    List<HHSysMenu> getMenuTreeByRoleCode(String roleCode);

    // 查询所有菜单（树形结构）
    List<HHSysMenu> getAllMenuTree();

    // 查询所有平级菜单
    List<HHSysMenu> getAllMenuList();

    // 新增：删除菜单（级联删除子菜单）
    boolean removeById(Integer menuId);
}