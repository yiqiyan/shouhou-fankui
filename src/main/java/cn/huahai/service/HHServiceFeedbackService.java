package cn.huahai.service;

import cn.huahai.entity.HHServiceFeedback;
import cn.huahai.vo.HHServiceFeedbackVO; // 新增VO类引用
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 售后反馈 Service 接口（继承IService，复用MyBatis-Plus基础方法）
 */
public interface HHServiceFeedbackService extends IService<HHServiceFeedback> {

    /**
     * 原有list方法：返回过滤逻辑删除的数据（复用父类实现）
     */
    @Override
    List<HHServiceFeedback> list();

    /**
     * 新增方法：返回全部数据（包含逻辑删除的）
     */
    List<HHServiceFeedback> listAll();

    /**
     * 新增核心方法：查询反馈详情（包含图片数据）
     * @param id 反馈ID
     * @return 包含图片的反馈VO对象
     */
    HHServiceFeedbackVO getDetailWithImages(Long id);
}
