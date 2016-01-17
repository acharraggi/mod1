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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class EventHandlerTick {
//	public static long lastServerTime = 0;
//	public static long waitTime = 30000;
//	@SubscribeEvent
//	public void onServerTick(ServerTickEvent event) {
//	// this most certainly WILL fire, even in single player, see for yourself:
//	//System.out.println("Ticking server"); - yup it fired!
//		
//		if(event.phase == TickEvent.Phase.START) { 			// new tick
//			long currentTime  = MinecraftServer.getCurrentTimeMillis();
//			if(lastServerTime == 0) {
//				lastServerTime = currentTime;  // first tick
//			}
//			else {
//				long elapsed = currentTime - lastServerTime;
//				//System.out.println("currentTime = " + currentTime + ", elapsed from last tick = " + elapsed);
//				if(elapsed > waitTime) {
//				    MinecraftServer minecraftServer = MinecraftServer.getServer();
//				    World world = minecraftServer.getEntityWorld();
//				    Random random = new Random();
//				    
//				    for (EntityPlayerMP player : (List<EntityPlayerMP>)minecraftServer.getConfigurationManager().playerEntityList) {
//				    	player.addChatMessage(new ChatComponentTranslation("msg.message_name.txt"));   // "Hello from server! Have a pig!"
//				    	Vec3 lookVec = player.getLookVec();  
//					    BlockPos playerPosition = player.getPosition();
//					    Vec3 bp = new Vec3(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ());
//					    Vec3 newLocation = bp.addVector(lookVec.xCoord*3, 0, lookVec.zCoord*3);  
//					    
//					    float yaw = random.nextFloat() * 360;
//					    float pitch = random.nextFloat() * 360;
//					    Entity entity = new EntityPig(world);
//					    entity.setLocationAndAngles(playerPosition.getX(), playerPosition.getY(), playerPosition.getZ(), yaw, pitch);
//					    //entity.setLocationAndAngles(newLocation.xCoord, newLocation.yCoord, newLocation.xCoord, yaw, pitch);
//				        world.spawnEntityInWorld(entity);
//				    }
//					lastServerTime = currentTime;  // reset
//				}
//			}
//		}
//	}
	
	private Thread t = null;
	
	@SubscribeEvent
	public void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		event.player.addChatMessage(new ChatComponentTranslation("msg.login.txt"));   // Hello from server. Get ready to run!
		System.out.println("about to start StubClient thread");
		t = new Thread(new StubClient(event.player));
		t.start();
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOutEvent(PlayerLoggedOutEvent event) {
		if (t != null) {
			t.interrupt();
		}
	}
}
