/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The File Watch Service Project licenses this file to you under the Apache License, version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *               http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package com.sohail.alam.http.server;

import com.sohail.alam.http.common.utils.HttpMethodCodes;
import com.sohail.alam.http.common.utils.LocalFileFetcherCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;
import static com.sohail.alam.http.common.utils.HttpResponseSender.*;
import static com.sohail.alam.http.common.utils.LocalFileFetcher.FETCHER;
import static com.sohail.alam.http.server.ServerProperties.PROP;
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
        LOGGER.debug("Handler Exception Caught: {}", cause.getMessage());
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

        switch (HttpMethodCodes.httpMethodCode.get(this.requestMethod)) {

            case HttpMethodCodes.CONNECT:
                if (PROP.IS_CONNECT_ALLOWED) {
                    processHttpConnectRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.DELETE:
                if (PROP.IS_DELETE_ALLOWED) {
                    processHttpDeleteRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.GET:
                if (PROP.IS_GET_ALLOWED) {
                    processHttpGetRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.HEAD:
                if (PROP.IS_HEAD_ALLOWED) {
                    processHttpHeadRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.OPTIONS:
                if (PROP.IS_OPTIONS_ALLOWED) {
                    processHttpOptionsRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.PATCH:
                if (PROP.IS_PATCH_ALLOWED) {
                    processHttpPatchRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.POST:
                if (PROP.IS_POST_ALLOWED) {
                    processHttpPostRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.PUT:
                if (PROP.IS_PUT_ALLOWED) {
                    processHttpPutRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            case HttpMethodCodes.TRACE:
                if (PROP.IS_TRACE_ALLOWED) {
                    processHttpTraceRequest();
                } else {
                    send405MethodNotAllowed(ctx, null, null, true);
                }
                break;

            default:
                send405MethodNotAllowed(ctx, null, null, true);
                break;
        }
    }

    /**
     * TODO: Process http connect request.
     */
    private void processHttpConnectRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http delete request.
     */
    private void processHttpDeleteRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http patch request.
     */
    private void processHttpPatchRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * Process http get request.
     */
    private void processHttpGetRequest() {
        FETCHER.fetch(this.requestUri, new FileFetcherCallback());
    }

    /**
     * TODO: Process http head request.
     */
    private void processHttpHeadRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http options request.
     */
    private void processHttpOptionsRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http post request.
     */
    private void processHttpPostRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http put request.
     */
    private void processHttpPutRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * TODO: Process http trace request.
     */
    private void processHttpTraceRequest() {
        send200OK(ctx, null, "Functionality to be provided soon!".getBytes(), true);
    }

    /**
     * The File fetcher callback class.
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
         * @param dataLength the data length
         */
        @Override
        public void fetchSuccess(String path, byte[] data, String mediaType, long dataLength) {
            LOGGER.debug("File {} Successfully fetched, length: {}", path, data.length);
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
            LOGGER.debug("Exception Caught: {}", cause.getMessage());
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
            LOGGER.debug("File Not Found: {}", cause.getMessage());
            // Check if access is denied or file not found
            if (cause.getMessage().contains("(Access is denied)")) {
                send403Forbidden(ctx, headers, null, true);
            } else {
                send404NotFound(ctx, headers, null, true);
            }
        }
    }
}
