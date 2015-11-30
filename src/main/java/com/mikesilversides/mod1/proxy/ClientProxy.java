package com.mikesilversides.mod1.proxy;

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
	    super.preInit();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.preInitClientOnly();
	  }
	  
	  public void init()
	  {
	    super.init();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.initClientOnly();
	  }

	  public void postInit()
	  {
	    super.postInit();
	    com.mikesilversides.mod1.hud_overlay.StartupClientOnly.postInitClientOnly();
	  }
	
	  @Override
	  public boolean playerIsInCreativeMode(EntityPlayer player) {
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
