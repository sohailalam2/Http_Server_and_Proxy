package com.sohail.alam.http.server;

import com.sohail.alam.http.server.handlers.HttpClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 9:50 PM
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    public HttpChannelInitializer() {
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        pipeline.addLast("ChunkedWriteHandler", new ChunkedWriteHandler());
        pipeline.addLast("HttpClientHandler", new HttpClientHandler());
    }
}
