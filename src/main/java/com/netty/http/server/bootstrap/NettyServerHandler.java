package com.netty.http.server.bootstrap;

import com.netty.http.server.common.Constant;
import com.netty.http.server.common.response.GeneralResponse;
import com.netty.http.server.common.utils.ResponseUtil;
import com.netty.http.server.router.HttpRouter;
import com.netty.http.server.router.HttpRouterDispatch;
import com.netty.http.server.router.HttpRouterTally;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private HttpRouter httpRouter;

    NettyServerHandler(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelRegistered");
        super.channelRegistered(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            if (Constant.FAVICON.equals(uri)) {
                ctx.close();
                return;
            }
            if (uri.contains(Constant.QUESTION)) {
                uri = uri.substring(0, uri.indexOf(Constant.QUESTION));
            }
            final HttpRouterDispatch<GeneralResponse> httpRouterDispatch = httpRouter.getRoute(new HttpRouterTally(uri, request.method()));
            if (httpRouterDispatch != null) {
                ResponseUtil.response(ctx, request, httpRouterDispatch.call(request));
            } else {
                ResponseUtil.notFound(ctx, request);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.handlerRemoved");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
