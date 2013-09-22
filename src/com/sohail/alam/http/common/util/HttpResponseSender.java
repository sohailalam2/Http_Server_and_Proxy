package com.sohail.alam.http.common.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 22/9/13
 * Time: 11:06 PM
 */
public class HttpResponseSender {

    public static ChannelFuture send(ChannelHandlerContext ctx, HttpResponseStatus status, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        final HttpResponse response;
        // Create the headers map if null
        if (headersMap == null) {
            headersMap = new HashMap<String, String>();
        }
        // If data is not null then add content length header and send Full Http Response
        if (data != null) {
            ByteBuf dataBuffer = copiedBuffer(data);
            response = new DefaultFullHttpResponse(HTTP_1_1, status, dataBuffer);
            headersMap.put(CONTENT_LENGTH, String.valueOf(data.length));
        }
        // If data is null then add content length header to 0
        else {
            response = new DefaultHttpResponse(HTTP_1_1, status);
            headersMap.put(CONTENT_LENGTH, String.valueOf(0));
        }
        // Iterate all headers from map and set response headers
        for (String header : headersMap.keySet()) {
            response.headers().set(header, headersMap.get(header));
        }
        // Send the response
        ChannelFuture future = ctx.channel().write(response);
        // Use default future listener if needed
        if (useDefaultListener) {
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        LOGGER.debug("Response sent successfully:\n{}", response);
                    } else {
                        LOGGER.debug("FAILED TO SEND RESPONSE!!\n{}", response);
                    }
                }
            });
        }

        return future;
    }

    public static ChannelFuture send200OK(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return send(ctx, OK, headersMap, data, useDefaultListener);
    }

    public static ChannelFuture send400BadRequest(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, BAD_REQUEST, headersMap, "BAD REQUEST".getBytes(), useDefaultListener);
        } else {
            return send(ctx, BAD_REQUEST, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send401Unauthorized(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, UNAUTHORIZED, headersMap, "UNAUTHORIZED".getBytes(), useDefaultListener);
        } else {
            return send(ctx, UNAUTHORIZED, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send403Forbidden(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, FORBIDDEN, headersMap, "FORBIDDEN".getBytes(), useDefaultListener);
        } else {
            return send(ctx, FORBIDDEN, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send404NotFound(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, NOT_FOUND, headersMap, "NOT FOUND".getBytes(), useDefaultListener);
        } else {
            return send(ctx, NOT_FOUND, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send405MethodNotAllowed(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, METHOD_NOT_ALLOWED, headersMap, "METHOD NOT ALLOWED".getBytes(), useDefaultListener);
        } else {
            return send(ctx, METHOD_NOT_ALLOWED, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send407ProxyAuthenticationRequired(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, PROXY_AUTHENTICATION_REQUIRED, headersMap, "PROXY AUTHENTICATION REQUIRED".getBytes(), useDefaultListener);
        } else {
            return send(ctx, PROXY_AUTHENTICATION_REQUIRED, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send408RequestTimeout(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, REQUEST_TIMEOUT, headersMap, "REQUEST TIMEOUT".getBytes(), useDefaultListener);
        } else {
            return send(ctx, REQUEST_TIMEOUT, headersMap, data, useDefaultListener);
        }
    }

    public static ChannelFuture send500InternalServerError(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        if (data == null) {
            return send(ctx, INTERNAL_SERVER_ERROR, headersMap, "INTERNAL SERVER ERROR".getBytes(), useDefaultListener);
        } else {
            return send(ctx, INTERNAL_SERVER_ERROR, headersMap, data, useDefaultListener);
        }

    }
}
