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

import com.sohail.alam.http.common.LoggerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * User: Sohail Alam
 * Version: 1.0.0
 * Date: 21/9/13
 * Time: 9:11 PM
 */
public class SetupServer {

    private static String ip;
    private static int port;

    public static SetupServer instance(String ip, int port) {
        SetupServer.ip = ip;
        SetupServer.port = port;
        return SingletonHolder.INSTANCE;
    }

    public void initialize() {
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final EventLoopGroup boss = new NioEventLoopGroup();
        final EventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, ServerProperties.PROP.SO_BACKLOG)
                .childOption(ChannelOption.TCP_NODELAY, ServerProperties.PROP.TCP_NODELAY)
                .childOption(ChannelOption.SO_KEEPALIVE, ServerProperties.PROP.SO_KEEPALIVE)
                .childOption(ChannelOption.SO_REUSEADDR, ServerProperties.PROP.SO_REUSEADDR)
                .childHandler(new HttpChannelInitializer());

        try {
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(ip, port)).sync();
            if (future.isSuccess()) {
                LoggerManager.LOGGER.info("Http Server Started Successfully @ {}:{}", ip, port);
            } else {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                LoggerManager.LOGGER.fatal("Http Server Start Failed. Can not bind to {}:{}", ip, port);
            }
        } catch (Exception e) {
            LoggerManager.LOGGER.fatal("Exception Caught while starting Http Server", e);
        }

    }

    private static interface SingletonHolder {
        public static final SetupServer INSTANCE = new SetupServer();
    }
}
