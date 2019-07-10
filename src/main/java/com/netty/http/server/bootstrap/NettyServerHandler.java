package com.netty.http.server.bootstrap;

import com.netty.http.server.common.response.GeneralResponse;
import com.netty.http.server.common.Global;
import com.netty.http.server.common.utils.ResponseUtils;
import com.netty.http.server.router.HttpRouter;
import com.netty.http.server.router.HttpRouterAction;
import com.netty.http.server.router.HttpRouterLabel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private HttpRouter httpRouter;

    NettyServerHandler(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("NettyServerHandler.handlerRemoved");
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            if (Global.FAVICON.equals(uri)) {
                return;
            }
            if (uri.contains(Global.QUESTION)) {
                uri = uri.substring(0, uri.indexOf(Global.QUESTION));
            }
            final HttpRouterAction<GeneralResponse> httpRouterAction = httpRouter.getRoute(new HttpRouterLabel(uri, request.method()));
            if (httpRouterAction != null) {
                if (httpRouterAction.isInjectionFullHttpRequest()) {
                    ResponseUtils.response(ctx, request, httpRouterAction.call(request));
                } else {
                    ResponseUtils.response(ctx, request, httpRouterAction.call());
                }
            } else {
                ResponseUtils.response(ctx, request, GeneralResponse.NOT_FOUND);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
