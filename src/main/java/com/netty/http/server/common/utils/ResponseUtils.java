package com.netty.http.server.common.utils;

import com.netty.http.server.common.response.GeneralResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class ResponseUtils {

    private ResponseUtils() {

    }

    public static void notFound(ChannelHandlerContext ctx, FullHttpRequest request) {
        response(ctx, request, GeneralResponse.NOT_FOUND);
    }

    public static void response(ChannelHandlerContext ctx, FullHttpRequest request, GeneralResponse generalResponse) {
        final boolean keepAlive = HttpUtil.isKeepAlive(request);
        final byte[] jsonByteByte = JsonUtils.toJson(generalResponse).getBytes();
        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, generalResponse.getResponseStatus(), Unpooled.wrappedBuffer(jsonByteByte));
        final HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);
        }
        ctx.flush();
    }
}
