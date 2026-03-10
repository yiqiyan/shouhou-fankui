package cn.huahai.config;

import cn.huahai.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    // 跨域配置（保持不变）
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // 核心修改：添加字典接口到放行白名单
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**") // 拦截所有/api开头的接口
                .excludePathPatterns(
                        // 原有放行接口
                        "/api/auth/login",
                        "/api/auth/logout",
                        // 新增：放行字典查询接口（免登录）
                        "/api/dict/contact-identity/list",
                        "/api/fault-type/list",
                        "/api/purchase-channel/list",
                        "/api/region/provinces",
                        "/api/region/children/**",
                        // 🔥 新增：放行提交反馈和图片上传接口（免登录）
                        "/api/service-feedback/add",
                        "/api/feedback-image/upload"
                );
    }
}

//package cn.huahai.config;
//
//import cn.huahai.interceptor.TokenInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private TokenInterceptor tokenInterceptor;
//
//    // 跨域配置（保持不变）
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
//
//    // 修复拦截器语法错误：补全括号，确保放行路径生效
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/api/**") // 拦截所有/api开头的接口
//                .excludePathPatterns("/api/auth/login", "/api/auth/logout"); // 放行登录/退出接口（补全右括号）
//    }
//}