package cn.huahai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.huahai.entity.HHPurchaseChannel;
import cn.huahai.mapper.HHPurchaseChannelMapper;
import cn.huahai.service.HHPurchaseChannelService;
import org.springframework.stereotype.Service;

/**
 * 购买渠道枚举表业务层实现类
 */
@Service
public class HHPurchaseChannelServiceImpl extends ServiceImpl<HHPurchaseChannelMapper, HHPurchaseChannel> implements HHPurchaseChannelService {
}