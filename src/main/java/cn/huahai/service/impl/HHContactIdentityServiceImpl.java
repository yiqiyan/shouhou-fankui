package cn.huahai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.huahai.entity.HHContactIdentity;
import cn.huahai.mapper.HHContactIdentityMapper;
import cn.huahai.service.HHContactIdentityService;
import org.springframework.stereotype.Service;

/**
 * 联系身份枚举表业务层实现类
 */
@Service
public class HHContactIdentityServiceImpl extends ServiceImpl<HHContactIdentityMapper, HHContactIdentity> implements HHContactIdentityService {
}