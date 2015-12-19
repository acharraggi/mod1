package com.mikesilversides.mod1.ServerTest;

import java.util.List;
import java.util.Random;

import com.mikesilversides.mod1.airstrike.StartupCommon;
import com.mikesilversides.mod1.airstrike.TargetEffectMessageToClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class EventHandlerTick {
	public static long lastServerTime = 0;
	public static long waitTime = 30000;
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
	// this most certainly WILL fire, even in single player, see for yourself:
	//System.out.println("Ticking server"); - yup it fired!
		
		if(event.phase == TickEvent.Phase.START) { 			// new tick
			long currentTime  = MinecraftServer.getCurrentTimeMillis();
			if(lastServerTime == 0) {
				lastServerTime = currentTime;  // first tick
			}
			else {
				long elapsed = currentTime - lastServerTime;
				//System.out.println("currentTime = " + currentTime + ", elapsed from last tick = " + elapsed);
				if(elapsed > waitTime) {
				    MinecraftServer minecraftServer = MinecraftServer.getServer();
				    World world = minecraftServer.getEntityWorld();
				    Random random = new Random();
				    
				    for (EntityPlayerMP player : (List<EntityPlayerMP>)minecraftServer.getConfigurationManager().playerEntityList) {
				    	player.addChatMessage(new ChatComponentTranslation("msg.message_name.txt"));   // "Hello from server!"
					    BlockPos playerPosition = player.getPosition();
					    float yaw = random.nextFloat() * 360;
					    float pitch = random.nextFloat() * 360;
					    Entity entity = new EntityPig(world);
					    entity.setLocationAndAngles(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), yaw, pitch);
				        world.spawnEntityInWorld(entity);
				    }
					lastServerTime = currentTime;  // reset
				}
			}
		}
	}

}
