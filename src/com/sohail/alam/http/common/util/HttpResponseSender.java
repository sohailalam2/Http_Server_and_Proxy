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
import static com.sohail.alam.http.common.util.LocalFileFetcher.FETCHER;
import static com.sohail.alam.http.server.ServerProperties.PROP;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 22/9/13
 * Time: 11:06 PM
 */
public class HttpResponseSender {

    /**
     * Send channel future.
     *
     * @param ctx                the ctx
     * @param status             the status
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
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
            // If no content length is supplied then calculate it and add it
            if (headersMap.get(CONTENT_LENGTH) == null) {
                headersMap.put(CONTENT_LENGTH, String.valueOf(data.length));
            }
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

    /**
     * Check before send.
     *
     * @param ctx                the ctx
     * @param status             the status
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     * @param defaultMessage     the default message
     *
     * @return the channel future
     */
    private static ChannelFuture checkBeforeSend(ChannelHandlerContext ctx, HttpResponseStatus status, Map<String, String> headersMap, byte[] data, boolean useDefaultListener, String defaultMessage) {
        if (data == null) {
            return send(ctx, status, headersMap, defaultMessage.getBytes(), useDefaultListener);
        } else {
            return send(ctx, status, headersMap, data, useDefaultListener);
        }
    }

    /**
     * Send 200 oK.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send200OK(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return send(ctx, OK, headersMap, data, useDefaultListener);
    }

    /**
     * Send 400 bad request.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send400BadRequest(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, BAD_REQUEST, headersMap, data, useDefaultListener, "BAD REQUEST");
    }

    /**
     * Send 401 unauthorized.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send401Unauthorized(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, UNAUTHORIZED, headersMap, data, useDefaultListener, "UNAUTHORIZED");
    }

    /**
     * Send 403 forbidden.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send403Forbidden(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, FORBIDDEN, headersMap, data, useDefaultListener, "FORBIDDEN");
    }

    /**
     * Send 404 not found.
     * If the data is null then this method automatically takes care of
     * sending a default 404 Page if configured and found,
     * otherwise sends a custom message.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send404NotFound(final ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        final Map<String, String> headers = new HashMap<String, String>();

        // If data is null then try to send the default 404 Page
        if (data == null) {
            FETCHER.fetch(PROP.default404Page(), new LocalFileFetcher.LocalFileFetcherCallback() {
                // Send the default 404 Page if found
                @Override
                public void fetchSuccess(String path, byte[] data, String mediaType, int dataLength) {
                    headers.put(CONTENT_TYPE, mediaType);
                    headers.put(CONTENT_LENGTH, String.valueOf(dataLength));
                    // Send data as 404 Not Found
                    send404NotFound(ctx, headers, data, true);
                }

                // Otherwise send a 404 NOT FOUND message
                @Override
                public void fileNotFound(String path, Throwable cause) {
                    headers.put(CONTENT_TYPE, MediaType.getType(".html"));
                    send404NotFound(ctx, headers, "404 NOT FOUND".getBytes(), true);
                }

                @Override
                public void exceptionCaught(String path, Throwable cause) {
                    headers.put(CONTENT_TYPE, MediaType.getType(".html"));
                    send404NotFound(ctx, headers, "404 NOT FOUND".getBytes(), true);
                }
            });
        }
        return send(ctx, NOT_FOUND, headersMap, data, useDefaultListener);
    }

    /**
     * Send 405 method not allowed.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send405MethodNotAllowed(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, METHOD_NOT_ALLOWED, headersMap, data, useDefaultListener, "METHOD NOT ALLOWED");
    }

    /**
     * Send 407 proxy authentication required.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send407ProxyAuthenticationRequired(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, PROXY_AUTHENTICATION_REQUIRED, headersMap, data, useDefaultListener, "PROXY AUTHENTICATION REQUIRED");
    }

    /**
     * Send 408 request timeout.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send408RequestTimeout(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, REQUEST_TIMEOUT, headersMap, data, useDefaultListener, "REQUEST TIMEOUT");
    }

    /**
     * Send 500 internal server error.
     *
     * @param ctx                the ctx
     * @param headersMap         the headers map
     * @param data               the data
     * @param useDefaultListener use default listener
     *
     * @return the channel future
     */
    public static ChannelFuture send500InternalServerError(ChannelHandlerContext ctx, Map<String, String> headersMap, byte[] data, boolean useDefaultListener) {
        return checkBeforeSend(ctx, INTERNAL_SERVER_ERROR, headersMap, data, useDefaultListener, "INTERNAL SERVER ERROR");
    }
}
