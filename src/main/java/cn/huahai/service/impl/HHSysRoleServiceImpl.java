package cn.huahai.service.impl;

import cn.huahai.entity.HHSysRole;
import cn.huahai.entity.HHSysRoleMenu;
import cn.huahai.mapper.HHSysRoleMapper;
import cn.huahai.mapper.HHSysRoleMenuMapper;
import cn.huahai.service.HHSysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HHSysRoleServiceImpl extends ServiceImpl<HHSysRoleMapper, HHSysRole> implements HHSysRoleService {

    private static final Logger log = LoggerFactory.getLogger(HHSysRoleServiceImpl.class);

    @Resource
    private HHSysRoleMapper roleMapper;

    @Resource
    private HHSysRoleMenuMapper roleMenuMapper;

    // 确保所有方法的签名和接口完全一致
    @Override
    public HHSysRole selectRoleById(Integer roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    @Override
    public List<HHSysRole> selectAllRoles() {
        return roleMapper.selectAllRoles();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(HHSysRole role) {
        role.setCreateTime(new Date());
        role.setStatus(1);
        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(HHSysRole role) {
        role.setCreateTime(null);
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleById(Integer roleId) {
        HHSysRole role = new HHSysRole();
        role.setRoleId(roleId);
        role.setStatus(0);
        return this.updateById(role);
    }

    @Override
    public List<Integer> selectMenuIdsByRoleId(Integer roleId) {
        return roleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenu(Integer roleId, List<Integer> menuIds) {
        log.info("分配菜单：roleId={}, menuIds={}", roleId, menuIds);

        if (roleId == null) {
            log.error("角色ID为空，分配失败");
            return false;
        }

        int deleteCount = roleMenuMapper.deleteByRoleId(roleId);
        log.info("删除原有菜单关联：{} 条", deleteCount);

        boolean insertSuccess = true;
        if (menuIds != null && !menuIds.isEmpty()) {
            List<HHSysRoleMenu> roleMenus = menuIds.stream().map(menuId -> {
                HHSysRoleMenu roleMenu = new HHSysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setCreateTime(new Date());
                return roleMenu;
            }).collect(Collectors.toList());

            int insertCount = roleMenuMapper.insertBatchSomeColumn(roleMenus);
            log.info("批量插入菜单关联：{} 条", insertCount);
            insertSuccess = insertCount > 0;
        } else {
            log.warn("菜单ID为空，跳过插入");
        }

        return insertSuccess;
    }
}
