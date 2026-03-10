package cn.huahai.mapper;

import cn.huahai.entity.HHServiceFeedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

/**
 * 售后反馈 Mapper 接口
 * 继承 BaseMapper 保留 MyBatis-Plus 基础功能，新增自定义查询所有数据的方法
 */
public interface HHServiceFeedbackMapper extends BaseMapper<HHServiceFeedback> {

    /**
     * 查询所有售后反馈数据（包含逻辑删除的）
     * @return 全部反馈列表
     */
    List<HHServiceFeedback> selectAll();
}