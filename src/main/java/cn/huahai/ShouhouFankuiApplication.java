package cn.huahai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 售后反馈系统 - Spring Boot 主启动类
 *
 * 关键配置：
 * - @MapperScan: 启用 MyBatis-Plus Mapper 接口自动扫描
 */
@SpringBootApplication
@MapperScan("cn.huahai.mapper")
public class ShouhouFankuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShouhouFankuiApplication.class, args);
    }
}