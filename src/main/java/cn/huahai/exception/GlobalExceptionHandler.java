package cn.huahai.exception;

import cn.huahai.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器（确保所有异常返回JSON格式）
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理Token相关异常
    @ExceptionHandler(TokenException.class)
    public Result handleTokenException(TokenException e) {
        return Result.error(e.getMessage()); // 返回{code:500,msg:"请先登录",data:null}
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.error(e.getMessage());
    }
}

//package cn.huahai.exception;
//
//import cn.huahai.common.Result;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
///**
// * 全局异常处理器（统一返回格式）
// */
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    /**
//     * 处理所有未捕获的异常
//     */
//    @ExceptionHandler(Exception.class)
//    public Result<?> handleException(Exception e) {
//        e.printStackTrace(); // 生产环境可改为日志记录
//        return Result.error("系统异常：" + e.getMessage());
//    }
//
//    /**
//     * 处理Token相关异常
//     */
//    @ExceptionHandler(TokenException.class)
//    public Result<?> handleTokenException(TokenException e) {
//        return Result.error(e.getMessage());
//    }
//}