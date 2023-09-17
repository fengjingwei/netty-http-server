package com.netty.http.server.router;

import com.google.common.collect.Maps;
import com.netty.http.server.annotation.RequestMapping;
import com.netty.http.server.common.Constant;
import com.netty.http.server.common.response.GeneralResponse;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

@Log4j2
public class HttpRouter extends ClassLoader {

    private static final int BUFFER_SIZE = 1024 * 8;
    private final Map<HttpRouterTally, HttpRouterDispatch<GeneralResponse<Object>>> httpRouterMapper = Maps.newConcurrentMap();
    private final String classpath = this.getClass().getResource(Constant.EMPTY).getPath();
    private final Map<String, Object> controllerBeans = Maps.newConcurrentMap();

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final String path = classpath + name.replaceAll("\\.", Constant.SLASH);
        byte[] bytes;
        try (InputStream ins = new FileInputStream(path)) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                final byte[] buffer = new byte[BUFFER_SIZE];
                int temp;
                while ((temp = ins.read(buffer)) != -1) {
                    out.write(buffer, 0, temp);
                }
                bytes = out.toByteArray();
            }
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
        return super.defineClass(name, bytes, 0, bytes.length);
    }

    public void addRouter(final Class<?> clazz) {
        try {
            final RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
            String clazzUri = Constant.EMPTY;
            if (classRequestMapping != null) {
                final String uri = classRequestMapping.uri();
                clazzUri = uri.startsWith(Constant.SLASH) ? uri : Constant.SLASH + uri;
            }
            final Method[] methods = clazz.getDeclaredMethods();
            for (Method invokeMethod : methods) {
                final Annotation[] annotations = invokeMethod.getAnnotations();
                for (Annotation annotation : annotations) {
                    final Class<? extends Annotation> annotationType = annotation.annotationType();
                    if (annotationType == RequestMapping.class) {
                        final RequestMapping methodRequestMapping = (RequestMapping) annotation;
                        final String methodUri = methodRequestMapping.uri();
                        final String httpMethod = methodRequestMapping.method().toString();
                        if (!controllerBeans.containsKey(clazz.getName())) {
                            controllerBeans.put(clazz.getName(), clazz.newInstance());
                        }
                        final HttpRouterDispatch<GeneralResponse<Object>> httpRouterDispatch = new HttpRouterDispatch<>(controllerBeans.get(clazz.getName()), invokeMethod);
                        final String requestUri = clazzUri + (methodUri.startsWith(Constant.SLASH) ? methodUri : Constant.SLASH + methodUri);
                        httpRouterMapper.put(new HttpRouterTally(requestUri, HttpMethod.valueOf(httpMethod)), httpRouterDispatch);
                    }
                }
            }

            if (log.isDebugEnabled()) {
                httpRouterMapper.forEach((key, value) -> log.info("加载控制层 => [{}], 请求路径 => [{}], 请求方法 => [{}]", value.getObject(), key.getUri(), key.getMethod()));
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public HttpRouterDispatch<GeneralResponse<Object>> getRoute(final HttpRouterTally httpRouterTally) {
        return httpRouterMapper.get(httpRouterTally);
    }
}
