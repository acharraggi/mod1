package com.mikesilversides.mod1.ServerTest;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class StartupServer {

	//public static long lastServerTime;

	 public static void preInitServer()
	 {
		 //lastServerTime = Minecraft.getMinecraft().getSystemTime();
		 //System.out.println("lastServerTime = " + lastServerTime);
	 }
	
	  public static void initServer()
	  {
		  //lastServerTime = Minecraft.getMinecraft().getSystemTime();
		  //System.out.println("lastServerTime = " + lastServerTime);
		  
		// register your event handler in main class during init or postinit:
		  FMLCommonHandler.instance().bus().register(new EventHandlerTick());
		  
		  try {
			EchoClient.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  public static void postInitServer()
	  {
		  //lastServerTime = Minecraft.getMinecraft().getSystemTime();
		  //System.out.println("lastServerTime = " + lastServerTime);
	  }
	  
}
