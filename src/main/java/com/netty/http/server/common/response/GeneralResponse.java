package com.netty.http.server.common.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeneralResponse<T> {

    public static final transient GeneralResponse<Object> NOT_FOUND = new GeneralResponse<>(HttpResponseStatus.NOT_FOUND, HttpResponseStatus.NOT_FOUND.reasonPhrase(), null);

    private static final transient HttpResponseStatus OK = HttpResponseStatus.OK;

    private static final transient HttpResponseStatus SERVER_ERROR = HttpResponseStatus.INTERNAL_SERVER_ERROR;

    private int status;

    private String message = "SUCCESS";

    private T data;

    private GeneralResponse(final HttpResponseStatus responseStatus, final T data) {
        this.status = responseStatus.code();
        this.data = data;
    }

    private GeneralResponse(final HttpResponseStatus responseStatus, final String message, final T data) {
        this.status = responseStatus.code();
        this.message = message;
        this.data = data;
    }

    public static <T> GeneralResponse<T> success(final T data) {
        return new GeneralResponse<>(OK, data);
    }

    public static GeneralResponse<Object> success() {
        return success(null);
    }

    public static <T> GeneralResponse<T> error(final String message) {
        return new GeneralResponse<>(SERVER_ERROR, message, null);
    }
}
