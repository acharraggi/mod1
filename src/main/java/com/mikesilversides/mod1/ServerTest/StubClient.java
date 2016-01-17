package com.mikesilversides.mod1.ServerTest;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
//package io.netty.example.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.minecraft.entity.player.EntityPlayer;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * Sends one start message when a connection is open to Stub1
 * Receives glitch messages from stub1
 */
public final class StubClient implements Runnable {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8008"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "4"));
    
    EntityPlayer stubPlayer;
    
    public StubClient(EntityPlayer player) {
    	stubPlayer = player;
    }

    public void run() {  
    	System.out.println("StubClient.run() called!");
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();

                     p.addLast(new LoggingHandler(LogLevel.INFO));
                     p.addLast(new StubClientHandler(stubPlayer));
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();   
    } catch (InterruptedException e) {
        // We've been interrupted: no more messages.
    	System.out.println("InterruptedException caught, do return");
    	return;
    }finally {
            // Shut down the event loop to terminate all threads.
    		System.out.println("doing group.shutdownGracefully()");
            group.shutdownGracefully();
        }
    }
}
