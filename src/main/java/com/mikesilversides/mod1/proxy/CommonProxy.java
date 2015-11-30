package com.mikesilversides.mod1.proxy;

import net.minecraft.entity.player.EntityPlayer;

public abstract class CommonProxy {
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.preInitCommon();
	  }
	  
	  /**
	   * Do your mod setup. Build whatever data structures you care about. Register recipes,
	   * send FMLInterModComms messages to other mods.
	   */
	  public void init()
	  {
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.initCommon();
	  }
	  
	  /**
	   * Handle interaction with other mods, complete your setup based on this.
	   */
	  public void postInit()
	  {
		  com.mikesilversides.mod1.hud_overlay.StartupCommon.postInitCommon();
	  }
	  
	  // helper to determine whether the given player is in creative mode
	  //  not necessary for most examples
	  abstract public boolean playerIsInCreativeMode(EntityPlayer player);

	public void registerRenders(){
		
	}
}
