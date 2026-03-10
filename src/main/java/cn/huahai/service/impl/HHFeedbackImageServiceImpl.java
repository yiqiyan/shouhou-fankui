package cn.huahai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.huahai.entity.HHFeedbackImage;
import cn.huahai.mapper.HHFeedbackImageMapper;
import cn.huahai.service.HHFeedbackImageService;
import org.springframework.stereotype.Service;

/**
 * 反馈图片表业务层实现类
 */
@Service
public class HHFeedbackImageServiceImpl extends ServiceImpl<HHFeedbackImageMapper, HHFeedbackImage> implements HHFeedbackImageService {
}