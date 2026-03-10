package cn.huahai.interceptor;

import cn.huahai.common.Result;
import cn.huahai.exception.TokenException;
import cn.huahai.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Token拦截器（校验登录状态）
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    // 统一JSON响应工具
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入 TokenInterceptor，请求路径：" + request.getRequestURI());
        // 1. 放行OPTIONS预检请求（跨域必加）
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 2. 排除登录/退出接口（无需Token）
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/auth/login") || requestURI.contains("/api/auth/logout")) {
            return true;
        }

        // 3. 获取Token：兼容两种格式（优先Authorization标准头）
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim(); // 去掉Bearer前缀+空格
        } else {
            token = request.getHeader("token"); // 兼容旧版token头
        }

        // 4. 空Token校验：返回标准JSON错误
        if (token == null || token.isEmpty()) {
            sendErrorResponse(response, "请先登录");
            return false;
        }

        // 5. Token有效性校验：捕获所有异常并返回友好提示
        try {
            if (!jwtUtil.validateToken(token)) {
                sendErrorResponse(response, "Token已过期或无效，请重新登录");
                return false;
            }
        } catch (TokenException e) {
            sendErrorResponse(response, e.getMessage());
            return false;
        } catch (Exception e) {
            sendErrorResponse(response, "Token解析失败，请重新登录");
            return false;
        }

        // 6. Token有效，放行请求
        return true;
    }

    /**
     * 统一返回JSON格式的错误响应（和前端约定格式）
     */
    private void sendErrorResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401未授权
        Result errorResult = Result.error(msg);
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}

//package cn.huahai.interceptor;
//
//import cn.huahai.exception.TokenException;
//import cn.huahai.util.JwtUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
///**
// * Token拦截器（校验登录状态）
// */
//@Component
//public class TokenInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 排除登录/退出接口
//        String requestURI = request.getRequestURI();
//        if (requestURI.contains("/api/auth/login") || requestURI.contains("/api/auth/logout")) {
//            return true;
//        }
//
//        // 获取Token
//        String token = request.getHeader("token");
//        if (token == null || token.isEmpty()) {
//            throw new TokenException("请先登录");
//        }
//
//        // 校验Token
//        if (!jwtUtil.validateToken(token)) {
//            throw new TokenException("Token已过期或无效");
//        }
//
//        return true;
//    }
//}