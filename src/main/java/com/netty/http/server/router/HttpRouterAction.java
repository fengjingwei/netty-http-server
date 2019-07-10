package com.netty.http.server.router;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@RequiredArgsConstructor
@Log4j2
public class HttpRouterAction<T> {

    @NonNull
    private Object object;

    @NonNull
    private Method method;

    private boolean injectionFullHttpRequest;

    public T call(Object... args) {
        try {
            return (T) method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("{}", e);
        }
        return null;
    }
}
