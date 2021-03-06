package com.mikesilversides.mod1.airstrike;
//package minecraftbyexample.mbe60_network_messages;

//import net.minecraft.block.BlockDirt;

import com.google.common.collect.ImmutableList;
import io.netty.channel.ChannelHandlerContext;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockContainer;
//import net.minecraft.block.BlockSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Random;

/**
 * The MessageHandlerOnServer is used to process the network message once it has arrived on the Server side.
 * WARNING!  In 1.8 the MessageHandler now runs in its own thread.  This means that if your onMessage code
 * calls any vanilla objects, it may cause crashes or subtle problems that are hard to reproduce.
 * Your onMessage handler should create a task which is later executed by the client or server thread as
 * appropriate - see below.
 * User: The Grey Ghost
 * Date: 15/01/2015
 */
public class MessageHandlerOnServer implements IMessageHandler<AirstrikeMessageToServer, IMessage>
{
  /**
   * Called when a message is received of the appropriate type.
   * CALLED BY THE NETWORK THREAD
   * @param message The message
   */
  public IMessage onMessage(final AirstrikeMessageToServer message, MessageContext ctx) {
    if (ctx.side != Side.SERVER) {
      System.err.println("AirstrikeMessageToServer received on wrong side:" + ctx.side);
      return null;
    }
    if (!message.isMessageValid()) {
      System.err.println("AirstrikeMessageToServer was invalid" + message.toString());
      return null;
    }

    // we know for sure that this handler is only used on the server side, so it is ok to assume
    //  that the ctx handler is a serverhandler, and that WorldServer exists.
    // Packets received on the client side must be handled differently!  See MessageHandlerOnClient

    final EntityPlayerMP sendingPlayer = ctx.getServerHandler().playerEntity;
    if (sendingPlayer == null) {
      System.err.println("EntityPlayerMP was null when AirstrikeMessageToServer was received");
      return null;
    }

    // This code creates a new task which will be executed by the server during the next tick,
    //  for example see MinecraftServer.updateTimeLightAndEntities(), just under section
    //      this.theProfiler.startSection("jobs");
    //  In this case, the task is to call messageHandlerOnServer.processMessage(message, sendingPlayer)
    final WorldServer playerWorldServer = sendingPlayer.getServerForPlayer();
    playerWorldServer.addScheduledTask(new Runnable() {
      public void run() {
        processMessage(message, sendingPlayer);
      }
    });

    return null;
  }

  // This message is called from the Server thread.
  //   It spawns a random number of the given projectile at a position above the target location
  void processMessage(AirstrikeMessageToServer message, EntityPlayerMP sendingPlayer)
  {
    // first send a message to all clients to render a "target" effect on the ground
//    StartupCommon.simpleNetworkWrapper.sendToDimension(msg, sendingPlayer.dimension);  // DO NOT USE sendToDimension, it is buggy
//    as of build 1419 - see https://github.com/MinecraftForge/MinecraftForge/issues/1908

    int dimension = sendingPlayer.dimension;
    MinecraftServer minecraftServer = sendingPlayer.mcServer;
    for (EntityPlayerMP player : (List<EntityPlayerMP>)minecraftServer.getConfigurationManager().playerEntityList) {
      TargetEffectMessageToClient msg = new TargetEffectMessageToClient(message.getTargetCoordinates());   // must generate a fresh message for every player!
      if (dimension == player.dimension) {
    	//player.addChatMessage();
//TODO: this won't work, only checks when msg exchanged. Need ServerTimeTick or something
//    	if(Math.floorMod(Minecraft.getMinecraft().getSystemTime(), 30) == 0) {
//    		player.addChatMessage(new ChatComponentTranslation("msg.message_name.txt"));
//    	}
        StartupCommon.simpleNetworkWrapper.sendTo(msg, player);
      }
    }

    // spawn projectiles
    Random random = new Random();
    final int MAX_NUMBER_OF_PROJECTILES = 20;
    final int MIN_NUMBER_OF_PROJECTILES = 2;
    //int numberOfProjectiles = MIN_NUMBER_OF_PROJECTILES + random.nextInt(MAX_NUMBER_OF_PROJECTILES - MIN_NUMBER_OF_PROJECTILES + 1);
    int numberOfProjectiles = 1;   //Mike
    for (int i = 0; i < numberOfProjectiles; ++i) {
      World world = sendingPlayer.worldObj;

      final double MAX_HORIZONTAL_SPREAD = 0.0; //Mike - was: 4.0
      final double MAX_VERTICAL_SPREAD = 0.0;  //Mike - was: 20.0
      final double RELEASE_HEIGHT_ABOVE_TARGET = 0.0;  //Mike - was: 40
      double xOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      //double zOffset = (random.nextDouble() * 2 - 1) * MAX_HORIZONTAL_SPREAD;
      double zOffset = 0.0;
      double yOffset = RELEASE_HEIGHT_ABOVE_TARGET + (random.nextDouble() * 2 - 1) * MAX_VERTICAL_SPREAD;
      Vec3 releasePoint = message.getTargetCoordinates().addVector(xOffset, yOffset, zOffset);
      float yaw = random.nextFloat() * 360;
      float pitch = random.nextFloat() * 360;

      System.out.println("target x="+releasePoint.xCoord+", "+	"y="+releasePoint.yCoord+", "+
    		  "z="+releasePoint.zCoord);
      
      // the following code will destroy a block under the player, 
      // Entity's use real numbers, blocks use Int's.
//	  Vec3 rP = message.getTargetCoordinates().addVector(0.0, -0.5, 0);
//	  BlockPos pos = new BlockPos(rP);
//	  releasePoint = new Vec3(pos.getX()+0.5,pos.getY(),pos.getZ()+0.5);
//      world.destroyBlock(pos, false);
	  // I've noticed that if the player is looking down they fall into the hole.
  	
      Entity entity;
      switch (message.getProjectile()) {
        case PIG: {
          entity = new EntityPig(world);
          entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, yaw, pitch);
          break;
        }
        case SNOWBALL: {
          entity = new EntitySnowball(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord);
          break;
        }
        case TNT: {
        	//BlockDirt signBlock = new BlockDirt();
        	//Vec3 p = message.getTargetCoordinates().addVector(0.0, 0.0, 1.0);
//        	BlockPos pos = new BlockPos(releasePoint);
//        	world.setBlockToAir(pos);
//        	entity = new EntityTNTPrimed(world, p.xCoord, p.yCoord, p.zCoord, sendingPlayer);
            entity = new EntityTNTPrimed(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, sendingPlayer);
          break;
        }
        case SNOWMAN: {
          entity = new EntitySnowman(world);
          entity.setLocationAndAngles(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, yaw, pitch);
          break;
        }
        case EGG: {
          entity = new EntityEgg(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord);
          break;
        }
        case FIREBALL: {
          final double Y_ACCELERATION = -0.5;
          entity = new EntityLargeFireball(world, releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, 0.0, Y_ACCELERATION, 0.0);
          break;
        }
        default: {
          System.err.println("Invalid projectile type in ServerMessageHandler:" + String.valueOf(message.getProjectile()));
          return;
        }
      }

      world.spawnEntityInWorld(entity);  
      
      // try adding sign
      //BlockSign mySign = new BlockSign();
      /*TileEntitySign mySign = new TileEntitySign();
      mySign.setWorldObj(world);
      mySign.setPlayer(sendingPlayer);
	  BlockPos pos = new BlockPos(releasePoint);
      mySign.setPos(pos);
      mySign.updateContainingBlockInfo();
      world.addTileEntity(mySign);*/
      // is that enough to create the sign? Nope - didn't see it
  
      final float VOLUME = 10000.0F;
      final float PITCH = 0.8F + random.nextFloat() * 0.2F;
      world.playSoundEffect(releasePoint.xCoord, releasePoint.yCoord, releasePoint.zCoord, "ambient.weather.thunder", VOLUME, PITCH);
    }

    return;
  }
}
