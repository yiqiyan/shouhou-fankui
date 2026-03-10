package cn.huahai.service.impl;

import cn.huahai.entity.HHRegion;
import cn.huahai.mapper.HHRegionMapper;
import cn.huahai.service.HHRegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HHRegionServiceImpl extends ServiceImpl<HHRegionMapper, HHRegion> implements HHRegionService {

    /**
     * 查询所有省份（层级=1）
     */
    @Override
    public List<HHRegion> listProvinces() {
        return this.list(
                new LambdaQueryWrapper<HHRegion>()
                        // 核心修正：用 BigDecimal 匹配数据库 decimal 类型
                        .eq(HHRegion::getNodeLevel, new BigDecimal(1))
                        .orderByAsc(HHRegion::getNoderOrder)
        );
    }

    /**
     * 根据父级编码查询下级地址
     */
    @Override
    public List<HHRegion> listChildrenByParentCode(String parentCode) {
        return this.list(
                new LambdaQueryWrapper<HHRegion>()
                        .eq(HHRegion::getCodeParent, parentCode)
                        .orderByAsc(HHRegion::getNoderOrder)
        );
    }

    /**
     * 根据编码查询单个地址
     */
    @Override
    public HHRegion getByNodeCode(String nodeCode) {
        return this.getOne(
                new LambdaQueryWrapper<HHRegion>().eq(HHRegion::getNodeCode, nodeCode)
        );
    }
}