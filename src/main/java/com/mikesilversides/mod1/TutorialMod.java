package com.mikesilversides.mod1;

import com.mikesilversides.mod1.init.TutorialBlocks;
import com.mikesilversides.mod1.init.TutorialItems;
import com.mikesilversides.mod1.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

//@Mod(modid = MinecraftByExample.MODID, version = MinecraftByExample.VERSION,
//guiFactory= MinecraftByExample.GUIFACTORY)  //delete guiFactory if MBE70 not present and you don't have a configuration GUI

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
	guiFactory = Reference.GUIFACTORY)
public class TutorialMod {
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		TutorialBlocks.init();
		TutorialBlocks.register();
		TutorialItems.init();
		TutorialItems.register();
		proxy.preInit();   //Mike- add proxy init's to get stuff working
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		proxy.init();
		proxy.registerRenders();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit();
	}
}
