package com.mikesilversides.mod1.ServerTest;

import java.util.Random;

import com.mikesilversides.mod1.airstrike.AirstrikeMessageToServer;

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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Handler implementation for the stub1 client.  It initiates the conversation
 * by sending a Start command, waiting for an ack, then receiving glitch commands.
 */
public class StubClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;
    private boolean startSent = false;
    private EntityPlayer stubClientPlayer;
    Random random = new Random();

    /**
     * Creates a client-side handler.
     */
    public StubClientHandler(EntityPlayer player) {
    	stubClientPlayer = player;
        firstMessage = Unpooled.buffer(EchoClient.SIZE);
        firstMessage.writeByte(1);  // 1=start
    }

    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
        startSent = true;
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ctx.write(msg);
    	ByteBuf in = (ByteBuf) msg;
    	final Byte inByte = in.readByte();
    	System.out.println("received from stub1 byte = " + inByte);
    	in.release();
    	
    	if(startSent) {
    		if(inByte == 2) {  // 2=ack
    			System.out.println("received ack from stub1");
    			startSent = false;
    		}
    		else {
    			System.out.println("unexpected reply received, closing connection");
    			ctx.close();
    			stubClientPlayer.addChatMessage(new ChatComponentText("unexpected stub1 reply received, connection closed"));
    		}
    	}
    	else {
    		//stubClientPlayer.addChatMessage(new ChatComponentText("stub1 sent byte = "+inByte));
    		
    	    // This code creates a new task which will be executed by the server during the next tick,
    	    //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section
    	    //      this.theProfiler.startSection("jobs");
    	    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
    	    //final WorldServer playerWorldServer = sendingPlayer.getServerForPlayer();
    	    //playerWorldServer.addScheduledTask(new Runnable() {
    		MinecraftServer.getServer().addScheduledTask(new Runnable() {
    	      public void run() {
    	        processMessage(inByte);
    	      }
    	    });
    	}
    }
    
    void processMessage(Byte inByte)
    {
	    MinecraftServer minecraftServer = MinecraftServer.getServer();
	    World world = minecraftServer.getEntityWorld();
    	
    	Vec3 lookVec = stubClientPlayer.getLookVec();  
	    BlockPos playerPosition = stubClientPlayer.getPosition();
	    Vec3 bp = new Vec3(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ());
	    Vec3 newLocation = bp.addVector(lookVec.xCoord*3, 0, lookVec.zCoord*3); 
	    float yaw = random.nextFloat() * 360;
	    float pitch = random.nextFloat() * 360;
	    
	    Entity entity = new EntityPig(world);
	    entity.setLocationAndAngles(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), yaw, pitch);
        
	    
	    switch (inByte) {
        case 3: {
        	stubClientPlayer.addChatMessage(new ChatComponentText("stub1 says 'Have a Pig!'"));
          entity = new EntityPig(world);
          entity.setLocationAndAngles(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), yaw, pitch);
          break;
        }
        case 4: {
        	stubClientPlayer.addChatMessage(new ChatComponentText("stub1 says 'It's cold out here!'"));
            entity = new EntitySnowman(world);
            entity.setLocationAndAngles(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), yaw, pitch);
            break;
          }
        case 5: {
        	stubClientPlayer.addChatMessage(new ChatComponentText("stub1 says 'Look out!'"));
            entity = new EntityTNTPrimed(world, playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), stubClientPlayer);
          break;
        }
        default: {
          System.err.println("Invalid message in StubClientHandler:" + inByte);
          return;
        }
	    }	    
	    world.spawnEntityInWorld(entity);

    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//       //ctx.flush(); // this was needed because of the write 
//       //do I need a flush() on a read??
//
//        //maybe try the following. nope, still hung, back to using close 
//        //ctx.fireChannelReadComplete();
//    	
//       //ctx.close(); // close after reading back. 
//       //Mike: without the close it hung during startup after getting the reply
//       
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}