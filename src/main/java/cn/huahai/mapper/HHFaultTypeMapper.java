package cn.huahai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.huahai.entity.HHFaultType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 故障类型枚举表Mapper接口
 */
@Mapper
public interface HHFaultTypeMapper extends BaseMapper<HHFaultType> {
}