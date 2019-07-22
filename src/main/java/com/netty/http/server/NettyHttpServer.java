package com.netty.http.server;

import com.netty.http.server.bootstrap.NettyServer;

public class NettyHttpServer {

    public static void main(String[] args) {
        new NettyServer().start();
    }
}