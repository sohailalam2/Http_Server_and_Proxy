package com.sohail.alam.http.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static com.sohail.alam.http.common.util.HttpResponseSender.*;
import static com.sohail.alam.http.common.util.LocalFileFetcher.FETCHER;
import static com.sohail.alam.http.common.util.LocalFileFetcher.LocalFileFetcherCallback;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 9:45 PM
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpRequest request;
    private ChannelHandlerContext ctx;
    private HttpMethod requestMethod;
    private String requestUri;

    /**
     * Instantiates a new Http client handler.
     */
    public HttpClientHandler() {
        LOGGER.trace("Http Client Handler Initialized");
    }

    /**
     * Channel active.
     *
     * @param ctx the ctx
     *
     * @throws Exception the exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    /**
     * Message received.
     *
     * @param ctx the ctx
     * @param msg the msg
     *
     * @throws Exception the exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // If decoder failed
        if (!msg.getDecoderResult().isSuccess()) {
            LOGGER.debug("Could not decode received request");
            send400BadRequest(ctx, null, "Could not decode received request".getBytes(), true);
            return;
        }
        // If the msg is a HttpRequest then process it
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            processHttpRequest();
        }
    }

    /**
     * Exception caught.
     *
     * @param ctx   the ctx
     * @param cause the cause
     *
     * @throws Exception the exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        send500InternalServerError(ctx, null, null, true);
    }

    /**
     * Process http request.
     */
    private void processHttpRequest() {
        this.requestMethod = request.getMethod();
        this.requestUri = request.getUri();
        LOGGER.info("Request Received: => {} => {} => {}", this.ctx.channel().remoteAddress(), this.requestMethod, this.requestUri);
        LOGGER.debug("Processing Http Request:\n{}", request);
        FETCHER.fetch(this.requestUri, new FileFetcherCallback());
    }

    /**
     * The type File fetcher callback.
     */
    private class FileFetcherCallback implements LocalFileFetcherCallback {

        private Map<String, String> headers = new HashMap<String, String>();

        /**
         * Fetch success is called when a file is read successfully and
         * the data is ready to be delivered.
         *
         * @param path       the path from which the file was read (Normalized Path)
         * @param data       the data as byte array
         * @param mediaType  the media type
         * @param dataLength
         */
        @Override
        public void fetchSuccess(String path, byte[] data, String mediaType, int dataLength) {
            LOGGER.debug("File Successfully fetched, length: {}", data.length);
            headers.put(CONTENT_TYPE, mediaType);
            headers.put(CONTENT_LENGTH, String.valueOf(dataLength));
            send200OK(ctx, headers, data, true);
        }

        /**
         * Exception caught.
         *
         * @param path  the path
         * @param cause the cause
         */
        @Override
        public void exceptionCaught(String path, Throwable cause) {
            if (cause.getMessage().contains("Access is denied")) {
                send500InternalServerError(ctx, headers, "ACCESS DENIED".getBytes(), true);
            } else {
                send500InternalServerError(ctx, headers, null, true);
            }
        }

        /**
         * Fetch failed is called whenever there was an error reading the file
         *
         * @param path  the path from which the file was to be read (Normalized Path)
         * @param cause the throwable object containing the cause
         */
        @Override
        public void fileNotFound(String path, final Throwable cause) {
            LOGGER.debug("Exception Caught: {}", cause.getMessage());
            // Check if access is denied or file not found
            if (cause.getMessage().contains("(Access is denied)")) {
                send403Forbidden(ctx, headers, null, true);
            } else {
                send404NotFound(ctx, headers, null, true);
            }
        }
    }
}