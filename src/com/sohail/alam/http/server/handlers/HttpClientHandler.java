package com.sohail.alam.http.server.handlers;

import com.sohail.alam.http.common.MediaType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static com.sohail.alam.http.common.util.HttpResponseSender.*;
import static com.sohail.alam.http.server.LocalFileFetcher.FETCHER;
import static com.sohail.alam.http.server.LocalFileFetcher.LocalFileFetcherCallback;
import static com.sohail.alam.http.server.ServerProperties.PROP;
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

    public HttpClientHandler() {
        LOGGER.trace("Http Client Handler Initialized");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // If decoder failed
        if (!msg.getDecoderResult().isSuccess()) {
            LOGGER.debug("Could not decode received request");
            // TODO: send error response
            return;
        }
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            processHttpRequest();
        }
    }

    private void processHttpRequest() {
        LOGGER.debug("Processing Http Request:\n{}", request);
        this.requestMethod = request.getMethod();
        if (request.getUri().endsWith("/")) {
            this.requestUri = request.getUri() + PROP.defaultPage();
        } else {
            this.requestUri = request.getUri();
        }

        FETCHER.fetch(this.requestUri, new FileFetcherCallback());
    }

    private String parseFileType(String uri) {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private class FileFetcherCallback implements LocalFileFetcherCallback {

        private Map<String, String> headers = new HashMap<String, String>();

        /**
         * Fetch success is called when a file is read successfully and
         * the data is ready to be delivered.
         *
         * @param path the path from which the file was read (Normalized Path)
         * @param data the data as byte array
         */
        @Override
        public void fetchSuccess(String path, byte[] data) {
            LOGGER.debug("File Successfully fetched, length: {}", data.length);
            headers.put(CONTENT_TYPE, MediaType.getType(parseFileType(path)));
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

            FETCHER.fetch(PROP.page404Path(), new LocalFileFetcherCallback() {
                @Override
                public void fetchSuccess(String path, byte[] data) {
                    headers.put(CONTENT_TYPE, MediaType.getType(".html"));
                    send404NotFound(ctx, headers, data, true);
                }

                @Override
                public void fileNotFound(String path, Throwable cause) {
                    send404NotFound(ctx, headers, null, true);
                }

                @Override
                public void exceptionCaught(String path, Throwable cause) {
                    send500InternalServerError(ctx, headers, null, true);
                }
            });


        }
    }
}
