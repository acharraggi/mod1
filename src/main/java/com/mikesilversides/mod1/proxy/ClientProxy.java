package com.mikesilversides.mod1.proxy;
// added HUD init

import com.mikesilversides.mod1.init.TutorialBlocks;
import com.mikesilversides.mod1.init.TutorialItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;


public class ClientProxy extends CommonProxy{
	
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {
		  System.out.println("ClientProxy.preInit called");
	    super.preInit();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.preInitClientOnly();
	    com.mikesilversides.mod1.airstrike.StartupClientOnly.preInitClientOnly();
	  }
	  
	  public void init()
	  {
		  System.out.println("ClientProxy.init called");
	    super.init();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.initClientOnly();
	    com.mikesilversides.mod1.airstrike.StartupClientOnly.initClientOnly();
	  }

	  public void postInit()
	  {
		  System.out.println("ClientProxy.postInit called");
	    super.postInit();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.postInitClientOnly();
	    com.mikesilversides.mod1.airstrike.StartupClientOnly.postInitClientOnly();
	  }
	
	  @Override
	  public boolean playerIsInCreativeMode(EntityPlayer player) {
		  System.out.println("ClientProxy.playerIsInCreativeMode called");
	    if (player instanceof EntityPlayerMP) {
	      EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
	      return entityPlayerMP.theItemInWorldManager.isCreative();
	    } else if (player instanceof EntityPlayerSP) {
	      return Minecraft.getMinecraft().playerController.isInCreativeMode();
	    }
	    return false;
	  }

	  
	@Override
	public void registerRenders(){
		TutorialBlocks.registerRenders();
		TutorialItems.registerRenders();
	}
}
