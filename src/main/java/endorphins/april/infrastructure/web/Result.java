package endorphins.april.infrastructure.web;

import lombok.Data;

/**
 * <描述>
 *
 * @author timothy.yang cloudwise
 * @since 2022-11-22 10:59
 */
@Data
public class Result<T> {

    private static final int SUCCESS_CODE = 200;
    private static final int CLIENT_ERROR_CODE = 400;
    private static final int SYS_ERROR_CODE = 500;
    private static final int UNKNOWN_ERROR_CODE = 600;

    private int code;

    private String message;

    private T data;

    Result(final int code, final String message, final T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> emptyFailResult() {
        return Result.<T>builderByFail(UNKNOWN_ERROR_CODE).build();
    }

    public static <T> Result<T> emptySuccessResult() {
        return Result.<T>builderBySuccess().build();
    }


    private static <T> ResultBuilder<T> builder(int code) {
        ResultBuilder builder = new ResultBuilder();
        return builder;
    }

    public static <T> ResultBuilder<T> builderBySuccess() {
        return builder(SUCCESS_CODE);
    }

    public static <T> ResultBuilder<T> builderByFail(int code) {
        return builder(code);
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public static class ResultBuilder<T> {
        private int code;
        private String message;
        private T data;

        ResultBuilder() {
        }

        public Result.ResultBuilder<T> code(final int code) {
            this.code = code;
            return this;
        }

        public Result.ResultBuilder<T> message(final String message) {
            this.message = message;
            return this;
        }

        public Result.ResultBuilder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public Result<T> build() {
            return new Result(this.code, this.message, this.data);
        }

    }
}
