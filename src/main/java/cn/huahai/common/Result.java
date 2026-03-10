package cn.huahai.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 返回结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /** 成功状态码 */
    public static final int SUCCESS_CODE = 200;
    /** 通用错误状态码 */
    public static final int ERROR_CODE = 500;

    private Integer code;
    private String msg;
    private T data;

    // ========== 成功响应 ==========

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "操作成功", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(SUCCESS_CODE, msg, data);
    }

    // ========== 错误响应 ==========

    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, "操作失败", null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ERROR_CODE, msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // ========== 辅助方法 ==========

    /**
     * 判断当前结果是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code.equals(SUCCESS_CODE);
    }
}