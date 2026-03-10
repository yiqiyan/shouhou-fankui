package cn.huahai.mapper;

import cn.huahai.entity.HHRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址表Mapper（只查不改，继承BaseMapper即可）
 */
@Mapper
public interface HHRegionMapper extends BaseMapper<HHRegion> {
    // 无需自定义方法，BaseMapper的查询方法足够用
}