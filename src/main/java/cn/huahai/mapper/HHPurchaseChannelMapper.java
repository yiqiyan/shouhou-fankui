package cn.huahai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.huahai.entity.HHPurchaseChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购买渠道枚举表Mapper接口
 */
@Mapper
public interface HHPurchaseChannelMapper extends BaseMapper<HHPurchaseChannel> {
}