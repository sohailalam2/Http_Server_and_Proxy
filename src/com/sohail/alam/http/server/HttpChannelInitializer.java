/*
 * Copyright 2013 The Http Server & Proxy
 *
 *  The Http Server & Proxy Project licenses this file to you under the Apache License, version 2.0 (the "License");
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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import static com.sohail.alam.http.common.LoggerManager.LOGGER;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 9:50 PM
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    public HttpChannelInitializer() {
        LOGGER.trace("HttpChannelInitializer Constructor Initialized");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.debug("Exception Caught in Channel Initializer: " + cause.getMessage());
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        LOGGER.debug("Http Channel Initialized => Remote Address: {}", ch.remoteAddress());
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        pipeline.addLast("ChunkedWriteHandler", new ChunkedWriteHandler());
        pipeline.addLast("HttpClientHandler", new HttpClientHandler());
    }
}
