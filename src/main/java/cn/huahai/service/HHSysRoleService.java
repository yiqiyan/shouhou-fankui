package cn.huahai.service;

import cn.huahai.entity.HHSysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 系统角色Service接口（SpringBoot3.1适配）
 */
public interface HHSysRoleService extends IService<HHSysRole> {

    /**
     * 根据角色ID查询角色
     * @param roleId 角色ID
     * @return 角色信息
     */
    HHSysRole selectRoleById(Integer roleId);

    /**
     * 查询所有启用的角色
     * @return 角色列表
     */
    List<HHSysRole> selectAllRoles();

    /**
     * 新增角色
     * @param role 角色信息
     * @return 操作结果
     */
    boolean insertRole(HHSysRole role);

    /**
     * 修改角色
     * @param role 角色信息
     * @return 操作结果
     */
    boolean updateRole(HHSysRole role);

    /**
     * 删除角色
     * @param roleId 角色ID
     * @return 操作结果
     */
    boolean deleteRoleById(Integer roleId);

    /**
     * 根据角色ID查询关联的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Integer> selectMenuIdsByRoleId(Integer roleId);

    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 操作结果
     */
    boolean assignMenu(Integer roleId, List<Integer> menuIds);
}