package com.netty.http.server.common.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.netty.http.server.common.Global;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public interface RequestUtils {

    static Map<String, String> getParameterMap(final FullHttpRequest request) {
        final HttpMethod method = request.method();
        if (method == HttpMethod.GET) {
            final String uri = request.uri();
            if (StringUtils.isNotEmpty(uri)) {
                if (uri.contains(Global.QUESTION)) {
                    try {
                        final String decodeUri = URLDecoder.decode(uri, CharsetUtil.UTF_8.toString());
                        final String params = StringUtils.substringAfterLast(decodeUri, Global.QUESTION);
                        return Splitter.on(Global.AND).omitEmptyStrings().trimResults().withKeyValueSeparator(Global.EQUAL).split(params);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Maps.newHashMap();
    }

    static <T> T postEntity(final FullHttpRequest request, final Class<T> clazz) {
        final ByteBuf jsonBuf = request.content();
        final String json = jsonBuf.toString(CharsetUtil.UTF_8);
        return JsonUtils.fromJson(json, clazz);
    }
}
