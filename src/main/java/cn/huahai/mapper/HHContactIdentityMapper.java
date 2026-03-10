package cn.huahai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.huahai.entity.HHContactIdentity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 联系身份枚举表Mapper接口
 */
@Mapper
public interface HHContactIdentityMapper extends BaseMapper<HHContactIdentity> {
}