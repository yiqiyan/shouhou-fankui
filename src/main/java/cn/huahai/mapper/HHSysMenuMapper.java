// cn.huahai.mapper.HHSysMenuMapper.java
package cn.huahai.mapper;

import cn.huahai.entity.HHSysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface HHSysMenuMapper extends BaseMapper<HHSysMenu> {
    List<HHSysMenu> selectMenuListByRoleCode(@Param("roleCode") String roleCode);
}