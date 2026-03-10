package cn.huahai.service.impl;

import cn.huahai.entity.HHSysRole;
import cn.huahai.entity.HHSysUser;
import cn.huahai.mapper.HHSysRoleMapper;
import cn.huahai.mapper.HHSysUserMapper;
import cn.huahai.mapper.HHSysUserRoleMapper;
import cn.huahai.service.HHSysUserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 系统用户Service实现类
 */
@Service
public class HHSysUserServiceImpl extends ServiceImpl<HHSysUserMapper, HHSysUser> implements HHSysUserService {

    @Autowired
    private HHSysUserMapper hhSysUserMapper;

    @Autowired
    private HHSysUserRoleMapper hhSysUserRoleMapper;

    @Autowired
    private HHSysRoleMapper hhSysRoleMapper;

    // 原有：登录校验 - 根据用户名查用户
    @Override
    public HHSysUser getByUsername(String username) {
        return hhSysUserMapper.selectByUsername(username);
    }

    // 新增：密码校验方法（登录时使用）
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        // 兼容未加密的密码（过渡用，生产环境建议全量加密）
        if (!encodedPassword.startsWith("$2a$") && !encodedPassword.startsWith("$2b$") && !encodedPassword.startsWith("$2y$")) {
            return rawPassword.equals(encodedPassword);
        }
        // BCrypt密码校验
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    // 原有：根据用户ID查角色编码
    @Override
    public String getRoleCodeByUserId(Integer userId) {
        List<Integer> roleIds = hhSysUserRoleMapper.selectRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        HHSysRole role = hhSysRoleMapper.selectRoleById(roleIds.get(0));
        return role != null ? role.getRoleCode() : null;
    }

    // 新增：分页查询用户（适配用户管理Controller）
    @Override
    public IPage<HHSysUser> selectUserPage(IPage<HHSysUser> page, Wrapper<HHSysUser> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

    // 重写：新增用户 - 自动填充创建时间 + 密码加密
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(HHSysUser user) {
        if (user == null || !StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            return false;
        }

        user.setCreateTime(new Date()); // 自动填充创建时间
        // 核心：密码BCrypt加密（生产级安全）
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return super.save(user);
    }

    // 重写：编辑用户 - 禁止修改创建时间 + 密码为空则保留原密码
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(HHSysUser user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }

        // 1. 查询原用户信息，获取原密码
        HHSysUser oldUser = this.getById(user.getUserId());
        if (oldUser == null) {
            return false;
        }

        // 2. 禁止修改创建时间
        user.setCreateTime(null);

        // 3. 处理密码：空密码保留原密码，非空则加密
        if (!StringUtils.hasText(user.getPassword())) {
            // 密码为空，使用原密码（关键修复：不再设置为null，避免清空）
            user.setPassword(oldUser.getPassword());
        } else {
            // 密码不为空，加密后更新
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        }

        return super.updateById(user);
    }
}