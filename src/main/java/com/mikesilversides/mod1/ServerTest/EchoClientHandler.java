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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
        firstMessage = Unpooled.buffer(EchoClient.SIZE);
//        for (int i = 0; i < firstMessage.capacity(); i ++) {
//            firstMessage.writeByte((byte) i);
//            
//        }
        String s = new String("Echo test string");
        ByteBufUtils.writeUTF8String(firstMessage, s);
        System.out.println("msg to send = " + s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.write(msg);
    	ByteBuf in = (ByteBuf) msg;
    	System.out.println("returned msg = " + in.toString(io.netty.util.CharsetUtil.UTF_8));
    	in.release();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       //ctx.flush(); // this was needed because of the write 
       //do I need a flush() on a read??

        //maybe try the following. nope, still hung, back to using close 
        //ctx.fireChannelReadComplete();
    	
       ctx.close(); // close after reading back. 
       //Mike: without the close it hung during startup after getting the reply
       

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}