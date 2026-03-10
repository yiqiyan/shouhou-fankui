package cn.huahai.mapper;

import cn.huahai.entity.HHSysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 系统角色Mapper接口（适配SpringBoot3.1 + MyBatis-Plus + JDK21）
 */
public interface HHSysRoleMapper extends BaseMapper<HHSysRole> {

    /**
     * 根据角色ID查询角色信息
     * @param roleId 角色ID
     * @return 角色信息
     */
    HHSysRole selectRoleById(@Param("roleId") Integer roleId);

    /**
     * 查询所有启用状态的角色
     * @return 角色列表
     */
    List<HHSysRole> selectAllRoles();

    /**
     * 根据角色ID查询关联的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Integer> selectMenuIdsByRoleId(@Param("roleId") Integer roleId);
}