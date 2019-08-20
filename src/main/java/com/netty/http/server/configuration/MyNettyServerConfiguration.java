package com.netty.http.server.configuration;

import com.netty.http.server.bootstrap.NettyServer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MyNettyServerConfiguration {

    @Bean(initMethod = "start")
    public NettyServer nettyServer() {
        return new NettyServer();
    }
}
