package cn.huahai.exception;

/**
 * Token自定义异常
 */
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}