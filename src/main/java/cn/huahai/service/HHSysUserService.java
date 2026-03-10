package cn.huahai.service;

import cn.huahai.entity.HHSysUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户Service
 */
public interface HHSysUserService extends IService<HHSysUser> {
    /**
     * 根据用户名查询用户（原有登录逻辑）
     * @param username 用户名
     * @return 用户信息
     */
    HHSysUser getByUsername(String username);

    /**
     * 根据用户ID查询角色编码（原有角色逻辑）
     * @param userId 用户ID
     * @return 角色编码（如ADMIN/OPERATOR）
     */
    String getRoleCodeByUserId(Integer userId);

    /**
     * 分页查询用户（新增：适配用户管理CRUD）
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页用户列表
     */
    IPage<HHSysUser> selectUserPage(IPage<HHSysUser> page, @Param(Constants.WRAPPER) Wrapper<HHSysUser> queryWrapper);

    // 新增：密码校验方法（登录时使用）
    boolean checkPassword(String rawPassword, String encodedPassword);
}