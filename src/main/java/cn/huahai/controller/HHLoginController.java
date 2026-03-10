package cn.huahai.controller;

import cn.huahai.common.Result;
import cn.huahai.entity.HHSysMenu;
import cn.huahai.entity.HHSysUser;
import cn.huahai.exception.TokenException;
import cn.huahai.service.HHSysMenuService;
import cn.huahai.service.HHSysUserService;
import cn.huahai.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录控制器（登录+菜单权限）
 */
@RestController
@RequestMapping("/api/auth")
public class HHLoginController {

    @Autowired
    private HHSysUserService hhSysUserService;

    @Autowired
    private HHSysMenuService hhSysMenuService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录接口（关联用户表+角色表+菜单表）
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        // 1. 校验参数
        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return Result.error("用户名/密码不能为空");
        }

        // 2. 查询用户（关联hh_sys_user表）
        HHSysUser user = hhSysUserService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("账号不存在");
        }

        // 3. 校验密码（BCrypt加密校验）
        if (!jwtUtil.matchPassword(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        // 4. 查询用户角色编码（关联hh_sys_user_role+hh_sys_role表）
        String roleCode = hhSysUserService.getRoleCodeByUserId(user.getUserId());
        if (roleCode == null) {
            return Result.error("该用户未分配角色，无法登录");
        }

        // 5. 根据角色编码查询菜单（关联hh_sys_role_menu+hh_sys_menu表）
        List<HHSysMenu> menuTree = hhSysMenuService.getMenuTreeByRoleCode(roleCode);

        // 6. 生成JWT Token（替换原UUID）
        String token = jwtUtil.generateToken(user.getUsername(), roleCode);

        // 7. 构造返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(token); // JWT Token
        response.setUserInfo(new UserInfo(user.getUsername(), user.getRealName(), roleCode));
        response.setMenuList(menuTree);

        return Result.success(response);
    }

    /**
     * 退出登录（前端清空Token即可，后端无需处理）
     */
    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出成功");
    }

    // 登录入参DTO
    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }

    // 登录返回DTO
    @Data
    public static class LoginResponse {
        private String token;
        private UserInfo userInfo;
        private List<HHSysMenu> menuList;
    }

    // 用户信息DTO
    @Data
    public static class UserInfo {
        private String username;
        private String realName;
        private String roleCode;

        public UserInfo(String username, String realName, String roleCode) {
            this.username = username;
            this.realName = realName;
            this.roleCode = roleCode;
        }
    }
}