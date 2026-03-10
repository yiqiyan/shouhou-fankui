package cn.huahai.service.impl;

import cn.huahai.entity.HHFeedbackImage;
import cn.huahai.entity.HHServiceFeedback;
import cn.huahai.mapper.HHServiceFeedbackMapper;
import cn.huahai.service.HHFeedbackImageService;
import cn.huahai.service.HHServiceFeedbackService;
import cn.huahai.vo.HHServiceFeedbackVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 售后反馈 Service 实现类
 */
@Service
public class HHServiceFeedbackServiceImpl extends ServiceImpl<HHServiceFeedbackMapper, HHServiceFeedback> implements HHServiceFeedbackService {

    // 注入图片表Service（核心：关联查询图片）
    @Autowired
    private HHFeedbackImageService feedbackImageService;

    /**
     * 原有方法：返回过滤逻辑删除的数据
     */
    @Override
    public List<HHServiceFeedback> list() {
        // 复用MyBatis-Plus默认实现（自动过滤逻辑删除数据）
        return super.list();
    }

    /**
     * 新增方法：返回全部数据（包含逻辑删除的）
     */
    @Override
    public List<HHServiceFeedback> listAll() {
        // 手动构建查询条件，忽略逻辑删除
        QueryWrapper<HHServiceFeedback> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0).or().eq("is_deleted", 1); // 根据你的逻辑删除字段调整
        return this.list(wrapper);
    }

    /**
     * 核心实现：查询反馈详情（包含图片）
     */
    @Override
    public HHServiceFeedbackVO getDetailWithImages(Long id) {
        // 1. 查询主表数据
        HHServiceFeedback feedback = this.getById(id);
        if (feedback == null) {
            return null;
        }

        // 2. 转换为VO对象（避免暴露实体类多余字段）
        HHServiceFeedbackVO vo = new HHServiceFeedbackVO();
        BeanUtils.copyProperties(feedback, vo); // 复制主表所有字段

        // 兜底：手动赋值purchaseDate（防止BeanUtils转换失败）
        vo.setPurchaseDate(feedback.getPurchaseDate());

        // 3. 关联查询图片表（根据feedbackId）
        List<HHFeedbackImage> imageList = feedbackImageService.list(
                new QueryWrapper<HHFeedbackImage>().eq("feedback_id", id)
        );
        vo.setImageList(imageList); // 设置图片列表

        return vo;
    }
}