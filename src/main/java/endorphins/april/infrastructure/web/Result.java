package endorphins.april.infrastructure.web;

import lombok.Data;

import java.io.Serializable;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-11-22 10:59
 */
@Data
public class Result<T> implements Serializable {

    private static final int SUCCESS_CODE = 200;
    private static final int CLIENT_ERROR_CODE = 400;
    private static final int SYS_ERROR_CODE = 500;
    private static final int UNKNOWN_ERROR_CODE = 600;

    private int code;

    private String message;

    private T data;

    Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, null, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(SUCCESS_CODE, message, null);
    }
}
