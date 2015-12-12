package com.mikesilversides.mod1.proxy;
// added HUD init

import com.mikesilversides.mod1.init.TutorialBlocks;
import com.mikesilversides.mod1.init.TutorialItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;


public class ServerProxy extends CommonProxy{
	
	/**
	   * Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry
	   */
	  public void preInit()
	  {
		System.out.println("ServerProxy.preInit called");
	    super.preInit();
	  }
	  
	  public void init()
	  {
		System.out.println("ServerProxy.init called");
	    super.init();
	  }

	  public void postInit()
	  {
		System.out.println("ServerProxy.postInit called");
	    super.postInit();
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
//		TutorialBlocks.registerRenders();
//		TutorialItems.registerRenders();
	}
}
