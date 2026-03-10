// cn.huahai.mapper.HHSysUserMapper.java
package cn.huahai.mapper;

import cn.huahai.entity.HHSysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface HHSysUserMapper extends BaseMapper<HHSysUser> {
    // 根据用户名查用户
    HHSysUser selectByUsername(@Param("username") String username);
}