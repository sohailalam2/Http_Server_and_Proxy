package com.sohail.alam.http.server.handlers;

import com.sohail.alam.http.common.MediaType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static com.sohail.alam.http.server.LocalFileFetcher.FETCHER;
import static com.sohail.alam.http.server.LocalFileFetcher.LocalFileFetcherCallback;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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
        this.requestUri = request.getUri();

        FETCHER.fetch(this.requestUri, new FileFetcherCallback());
    }

    private void sendData(byte[] data, String contentType) {
        ByteBuf dataBuffer = copiedBuffer(data);
        final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, dataBuffer);
        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().set(CONTENT_LENGTH, dataBuffer.readableBytes());

        ChannelFuture future = this.ctx.channel().write(response);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LOGGER.debug("Response sent successfully:\n{}", response);
                } else {
                    LOGGER.debug("Failed to send response");
                }
            }
        });
    }

    private String parseFileType(String uri) {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private class FileFetcherCallback implements LocalFileFetcherCallback {
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
            sendData(data, MediaType.getType(parseFileType(path)));
        }

        /**
         * Fetch failed is called whenever there was an error reading the file
         *
         * @param path  the path from which the file was to be read (Normalized Path)
         * @param cause the throwable object containing the cause
         */
        @Override
        public void fetchFailed(String path, Throwable cause) {
            LOGGER.debug("Exception Caught: {}", cause.getMessage());
            ctx.channel().closeFuture();
        }
    }
}
