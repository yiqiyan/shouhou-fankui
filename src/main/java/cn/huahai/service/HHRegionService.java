package cn.huahai.service;

import cn.huahai.entity.HHRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 地址表Service
 */
public interface HHRegionService extends IService<HHRegion> {

    /**
     * 查询所有省份（层级=1）
     */
    List<HHRegion> listProvinces();

    /**
     * 根据父级编码查询下级地址（查市/区县）
     */
    List<HHRegion> listChildrenByParentCode(String parentCode);

    /**
     * 根据编码查询单个地址信息
     */
    HHRegion getByNodeCode(String nodeCode);
}