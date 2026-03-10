package cn.huahai.controller;

import cn.huahai.entity.HHSysUser;
import cn.huahai.entity.HHSysUserRole;
import cn.huahai.mapper.HHSysUserRoleMapper;
import cn.huahai.service.HHSysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户管理控制器
 * 核心：路径 /api/system/user 与前端请求完全对齐
 */
@Slf4j
@RestController
@RequestMapping("/api/system/user")
public class HHSysUserController {

    @Resource
    private HHSysUserService hhSysUserService;

    @Resource
    private HHSysUserRoleMapper hhSysUserRoleMapper;

    /**
     * 新增：登录接口（适配密码加密校验）
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginParams) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 参数校验
            String username = loginParams.get("username");
            String password = loginParams.get("password");
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                result.put("code", 400);
                result.put("msg", "用户名或密码不能为空");
                return ResponseEntity.badRequest().body(result);
            }

            // 2. 查询用户
            HHSysUser user = hhSysUserService.getByUsername(username);
            if (user == null) {
                result.put("code", 401);
                result.put("msg", "用户名不存在");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            // 3. 密码校验（兼容加密/未加密）
            boolean passwordMatch = hhSysUserService.checkPassword(password, user.getPassword());
            if (!passwordMatch) {
                result.put("code", 401);
                result.put("msg", "密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            // 4. 登录成功（实际项目中这里生成Token返回）
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("data", Map.of(
                    "userId", user.getUserId(),
                    "username", user.getUsername(),
                    "roleCode", hhSysUserService.getRoleCodeByUserId(user.getUserId())
            ));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("用户登录失败", e);
            result.put("code", 500);
            result.put("msg", "服务器错误，登录失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 1. 分页查询用户（支持用户名/真实姓名模糊搜索）
     */
    @GetMapping("/list")
    public ResponseEntity<IPage<HHSysUser>> getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName) {
        if (pageNum < 1) pageNum = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 10;

        try {
            Page<HHSysUser> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<HHSysUser> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(username)) {
                queryWrapper.like(HHSysUser::getUsername, username);
            }
            if (StringUtils.isNotBlank(realName)) {
                queryWrapper.like(HHSysUser::getRealName, realName);
            }
            queryWrapper.orderByDesc(HHSysUser::getCreateTime);

            IPage<HHSysUser> userPage = hhSysUserService.selectUserPage(page, queryWrapper);
            return ResponseEntity.ok(userPage);
        } catch (Exception e) {
            log.error("分页查询用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 2. 新增用户
     */
    @PostMapping
    public ResponseEntity<Boolean> addUser(@RequestBody HHSysUser user) {
        if (user == null || StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return ResponseEntity.badRequest().body(false);
        }

        try {
            boolean success = hhSysUserService.save(user);
            log.info("新增用户{}，结果：{}", user.getUsername(), success);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            log.error("新增用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * 3. 编辑用户
     */
    @PutMapping
    public ResponseEntity<Boolean> updateUser(@RequestBody HHSysUser user) {
        if (user == null || user.getUserId() == null) {
            return ResponseEntity.badRequest().body(false);
        }

        try {
            boolean success = hhSysUserService.updateById(user);
            log.info("编辑用户ID{}，结果：{}", user.getUserId(), success);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            log.error("编辑用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * 4. 删除用户
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId) {
        if (userId == null || userId == 1) {
            log.warn("尝试删除超级管理员或空ID，用户ID：{}", userId);
            return ResponseEntity.badRequest().body(false);
        }

        try {
            boolean success = hhSysUserService.removeById(userId);
            log.info("删除用户ID{}，结果：{}", userId, success);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    /**
     * 5. 根据ID查询单个用户（编辑回显用）
     */
    @GetMapping("/{userId}")
    public ResponseEntity<HHSysUser> getUserById(@PathVariable Integer userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            HHSysUser user = hhSysUserService.getById(userId);
            if (user != null) {
                user.setPassword(""); // 清空密码，安全防护
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("查询用户详情失败，用户ID：{}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 6. 登录校验专用：根据用户名查询用户
     */
    @GetMapping("/info/{username}")
    public ResponseEntity<HHSysUser> getUserByUsername(@PathVariable String username) {
        if (StringUtils.isBlank(username)) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            HHSysUser user = hhSysUserService.getByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("根据用户名查询用户失败，用户名：{}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 7. 根据用户ID查询角色编码
     */
    @GetMapping("/role/{userId}")
    public ResponseEntity<String> getRoleCodeByUserId(@PathVariable Integer userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            String roleCode = hhSysUserService.getRoleCodeByUserId(userId);
            return ResponseEntity.ok(roleCode);
        } catch (Exception e) {
            log.error("查询用户角色失败，用户ID：{}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 8. 分配角色（最终版：兼容所有参数类型）
     */
    @PostMapping("/assignRole")
    public ResponseEntity<Boolean> assignRole(@RequestBody Map<String, Object> params) {
        try {
            log.info("分配角色参数: {}", params);

            // 宽松解析参数
            Integer userId = null;
            List<Integer> roleIds = null;

            // 解析userId
            if (params.containsKey("userId")) {
                Object userIdObj = params.get("userId");
                if (userIdObj instanceof Number) {
                    userId = ((Number) userIdObj).intValue();
                } else if (userIdObj instanceof String) {
                    userId = Integer.parseInt((String) userIdObj);
                }
            }

            // 解析roleIds
            if (params.containsKey("roleIds") && params.get("roleIds") instanceof List<?>) {
                List<?> rawRoleIds = (List<?>) params.get("roleIds");
                roleIds = rawRoleIds.stream()
                        .filter(id -> id != null)
                        .map(id -> {
                            if (id instanceof Number) {
                                return ((Number) id).intValue();
                            } else if (id instanceof String) {
                                return Integer.parseInt((String) id);
                            }
                            return null;
                        })
                        .filter(id -> id != null)
                        .toList();
            }

            // 参数校验
            if (userId == null || roleIds == null || roleIds.isEmpty()) {
                log.warn("分配角色参数无效: userId={}, roleIds={}", userId, roleIds);
                return ResponseEntity.badRequest().body(false);
            }

            // 执行角色分配
            hhSysUserRoleMapper.deleteByUserId(userId);
            boolean success = true;
            Date now = new Date();
            for (Integer roleId : roleIds) {
                HHSysUserRole userRole = new HHSysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(now);
                if (hhSysUserRoleMapper.insert(userRole) <= 0) {
                    success = false;
                }
            }

            log.info("用户ID{}分配角色{}，结果：{}", userId, roleIds, success);
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            log.error("分配角色失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}