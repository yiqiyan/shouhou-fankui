package cn.huahai.mapper;

import cn.huahai.entity.HHSysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HHSysUserRoleMapper extends BaseMapper<HHSysUserRole> {
    // 根据用户ID查角色ID列表
    List<Integer> selectRoleIdsByUserId(@Param("userId") Integer userId);

    // 新增：根据用户ID删除角色关联
    int deleteByUserId(@Param("userId") Integer userId);
}