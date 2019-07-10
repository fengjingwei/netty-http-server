package com.netty.http.server.router;

import com.google.common.collect.Maps;
import com.netty.http.server.annotation.RequestMapping;
import com.netty.http.server.common.Global;
import com.netty.http.server.common.response.GeneralResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

@Log4j2
public class HttpRouter extends ClassLoader {

    private Map<HttpRouterLabel, HttpRouterAction<GeneralResponse>> httpRouterMapper = Maps.newConcurrentMap();

    private String classpath = this.getClass().getResource(StringUtils.EMPTY).getPath();

    private Map<String, Object> controllerBeans = Maps.newConcurrentMap();

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final String path = classpath + name.replaceAll("\\.", Global.SLASH);
        byte[] bytes;
        try (InputStream ins = new FileInputStream(path)) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[1024 * 5];
                int b;
                while ((b = ins.read(buffer)) != -1) {
                    out.write(buffer, 0, b);
                }
                bytes = out.toByteArray();
            }
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
        return super.defineClass(name, bytes, 0, bytes.length);
    }

    public void addRouter(final String controllerClass) {
        try {
            final Class<?> clazz = super.loadClass(controllerClass);
            final Method[] methods = clazz.getDeclaredMethods();
            for (Method invokeMethod : methods) {
                final Annotation[] annotations = invokeMethod.getAnnotations();
                for (Annotation annotation : annotations) {
                    final Class<? extends Annotation> annotationType = annotation.annotationType();
                    if (annotationType == RequestMapping.class) {
                        final RequestMapping requestMapping = (RequestMapping) annotation;
                        final String uri = requestMapping.uri();
                        final String httpMethod = requestMapping.method().toString().toUpperCase();
                        if (!controllerBeans.containsKey(clazz.getName())) {
                            controllerBeans.put(clazz.getName(), clazz.newInstance());
                        }
                        final HttpRouterAction httpRouterAction = new HttpRouterAction(controllerBeans.get(clazz.getName()), invokeMethod);
                        final Class[] params = invokeMethod.getParameterTypes();
                        if (params.length == 1 && params[0] == FullHttpRequest.class) {
                            httpRouterAction.setInjectionFullHttpRequest(true);
                        }
                        httpRouterMapper.put(new HttpRouterLabel(uri.startsWith(Global.SLASH) ? uri : Global.SLASH + uri, new HttpMethod(httpMethod)), httpRouterAction);
                    }
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    public HttpRouterAction getRoute(final HttpRouterLabel httpRouterLabel) {
        return httpRouterMapper.get(httpRouterLabel);
    }
}
