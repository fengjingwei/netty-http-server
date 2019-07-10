package com.netty.http.server.bootstrap;

import com.netty.http.server.common.utils.SpringContextHolder;
import com.netty.http.server.router.HttpRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetSocketAddress;

@Log4j2
@SpringBootConfiguration
public class NettyServer implements ServletContextListener {

    private static final Integer PORT = 8888;
    private final SpringContextHolder springContextHolder;
    private Channel serverChannel;
    private HttpRouter httpRouter = new HttpRouter();

    @Autowired
    public NettyServer(SpringContextHolder springContextHolder) {
        this.springContextHolder = springContextHolder;
    }

    public void start() {
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {

                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("httpServerCodec", new HttpServerCodec());
                            pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("httpContentCompressor", new HttpContentCompressor());
                            pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
                            pipeline.addLast("nettyServerHandler", new NettyServerHandler(httpRouter));
                        }
                    });

            serverChannel = bootstrap.bind(new InetSocketAddress(PORT)).sync().channel();
            log.info("开启{}端口成功", PORT);
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (serverChannel != null) {
            serverChannel.close();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        springContextHolder.loadControllerClass().forEach(httpRouter::addRouter);
        start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        stop();
    }
}
