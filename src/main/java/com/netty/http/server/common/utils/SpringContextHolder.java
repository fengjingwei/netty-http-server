package com.netty.http.server.common.utils;

import com.netty.http.server.annotation.RestController;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Lazy(value = false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    private List<Class<?>> controllerClass;
    private ApplicationContext applicationContext;

    public <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.controllerClass = loadControllerClass(applicationContext);
    }

    @Override
    public void destroy() {
        applicationContext = null;
    }

    private List<Class<?>> loadControllerClass(ApplicationContext ctx) {
        final Class<? extends Annotation> clazz = RestController.class;
        return ctx.getBeansWithAnnotation(clazz)
                .values().stream()
                .map(AopUtils::getTargetClass)
                .filter(cls -> Objects.nonNull(cls.getAnnotation(clazz)))
                .collect(Collectors.toList());
    }

    public List<Class<?>> loadControllerClass() {
        return controllerClass;
    }
}
