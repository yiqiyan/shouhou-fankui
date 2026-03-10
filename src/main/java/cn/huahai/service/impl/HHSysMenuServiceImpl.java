package cn.huahai.service.impl;

import cn.huahai.entity.HHSysMenu;
import cn.huahai.mapper.HHSysMenuMapper;
import cn.huahai.service.HHSysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HHSysMenuServiceImpl extends ServiceImpl<HHSysMenuMapper, HHSysMenu> implements HHSysMenuService {

    // 原有：根据角色编码查询树形菜单
    @Override
    public List<HHSysMenu> getMenuTreeByRoleCode(String roleCode) {
        List<HHSysMenu> menuList = baseMapper.selectMenuListByRoleCode(roleCode);
        return buildMenuTree(menuList);
    }

    // 新增：查询所有菜单（树形结构）
    @Override
    public List<HHSysMenu> getAllMenuTree() {
        LambdaQueryWrapper<HHSysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HHSysMenu::getStatus, 1)
                .orderByAsc(HHSysMenu::getSort);
        List<HHSysMenu> menuList = baseMapper.selectList(queryWrapper);
        return buildMenuTree(menuList);
    }

    // 新增：查询所有平级菜单（用于下拉选择父菜单）
    public List<HHSysMenu> getAllMenuList() {
        LambdaQueryWrapper<HHSysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HHSysMenu::getStatus, 1)
                .orderByAsc(HHSysMenu::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    // 新增：新增菜单（自动填充创建时间）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(HHSysMenu menu) {
        if (menu == null || !StringUtils.hasText(menu.getMenuName())) {
            return false;
        }
        // 默认父ID为0（顶级菜单）
        if (menu.getParentId() == null) {
            menu.setParentId(0);
        }
        // 默认排序为0
        if (menu.getSort() == null) {
            menu.setSort(0);
        }
        // 默认状态为1（启用）
        if (menu.getStatus() == null) {
            menu.setStatus(1);
        }
        menu.setCreateTime(new Date());
        return super.save(menu);
    }

    // 新增：编辑菜单（禁止修改创建时间）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(HHSysMenu menu) {
        if (menu == null || menu.getMenuId() == null) {
            return false;
        }
        menu.setCreateTime(null); // 禁止修改创建时间
        return super.updateById(menu);
    }

    // 新增：删除菜单（级联删除子菜单）
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Integer menuId) {
        if (menuId == null) {
            return false;
        }
        // 先删除子菜单
        LambdaQueryWrapper<HHSysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HHSysMenu::getParentId, menuId);
        baseMapper.delete(queryWrapper);
        // 再删除当前菜单
        return super.removeById(menuId);
    }

    // 私有方法：构建树形菜单
    private List<HHSysMenu> buildMenuTree(List<HHSysMenu> menuList) {
        List<HHSysMenu> treeMenu = new ArrayList<>();
        for (HHSysMenu menu : menuList) {
            if (menu.getParentId() == 0) {
                menu.setChildren(getChildren(menu.getMenuId(), menuList));
                treeMenu.add(menu);
            }
        }
        return treeMenu;
    }

    // 私有方法：递归获取子菜单
    private List<HHSysMenu> getChildren(Integer parentId, List<HHSysMenu> allMenu) {
        List<HHSysMenu> children = new ArrayList<>();
        for (HHSysMenu menu : allMenu) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(getChildren(menu.getMenuId(), allMenu));
                children.add(menu);
            }
        }
        return children;
    }
}