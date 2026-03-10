package cn.huahai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.huahai.entity.HHFaultType;
import cn.huahai.mapper.HHFaultTypeMapper;
import cn.huahai.service.HHFaultTypeService;
import org.springframework.stereotype.Service;

/**
 * 故障类型枚举表业务层实现类
 */
@Service
public class HHFaultTypeServiceImpl extends ServiceImpl<HHFaultTypeMapper, HHFaultType> implements HHFaultTypeService {
}