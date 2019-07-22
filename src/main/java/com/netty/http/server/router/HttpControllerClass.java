package com.netty.http.server.router;

import com.google.common.collect.Lists;

import java.util.List;

public final class HttpControllerClass {

    private static final List<String> CONTROLLER_CLASS = Lists.newLinkedList();

    static {
        CONTROLLER_CLASS.add("com.netty.http.server.order.controller.OrderController");
    }

    public static List<String> loadControllerClass() {
        return CONTROLLER_CLASS;
    }
}
