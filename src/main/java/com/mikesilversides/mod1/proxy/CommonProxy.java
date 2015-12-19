package com.mikesilversides.mod1.proxy;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy {
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {
		  com.mikesilversides.mod1.mbe70_configuration.StartupCommon.preInitCommon();
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.preInitCommon();
		  com.mikesilversides.mod1.airstrike.StartupCommon.preInitCommon();
		  com.mikesilversides.mod1.ServerTest.StartupServer.preInitServer();
	  }
	  
	  /**
	   * Do your mod setup. Build whatever data structures you care about. Register recipes,
	   * send FMLInterModComms messages to other mods.
	   */
	  public void init()
	  {
		  com.mikesilversides.mod1.mbe70_configuration.StartupCommon.initCommon();
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.initCommon();
		  com.mikesilversides.mod1.airstrike.StartupCommon.initCommon();
		  com.mikesilversides.mod1.ServerTest.StartupServer.initServer();
	  }
	  
	  /**
	   * Handle interaction with other mods, complete your setup based on this.
	   */
	  public void postInit()
	  {
		  com.mikesilversides.mod1.mbe70_configuration.StartupCommon.postInitCommon();
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.postInitCommon();
		  com.mikesilversides.mod1.airstrike.StartupCommon.postInitCommon();
		  com.mikesilversides.mod1.ServerTest.StartupServer.postInitServer();
	  }
	  
	  // helper to determine whether the given player is in creative mode
	  //  not necessary for most examples
	  abstract public boolean playerIsInCreativeMode(EntityPlayer player);

	public void registerRenders(){
		
	}
}
