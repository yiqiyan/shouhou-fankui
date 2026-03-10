package cn.huahai.mapper;

import cn.huahai.entity.HHSysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HHSysRoleMenuMapper extends BaseMapper<HHSysRoleMenu> {

    /**
     * 根据角色ID删除所有菜单关联
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 批量插入角色菜单关联（MyBatis-Plus 扩展方法）
     * @param entityList 角色菜单列表
     * @return 影响行数
     */
    int insertBatchSomeColumn(List<HHSysRoleMenu> entityList);

    /**
     * 根据角色ID查询关联的菜单ID
     */
    List<Integer> selectMenuIdsByRoleId(@Param("roleId") Integer roleId);
}