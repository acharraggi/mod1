package com.mikesilversides.mod1.hud_overlay;

import net.minecraftforge.fml.common.registry.GameRegistry;

 /**
 * @author Nephroid
 *
 * User: Nephroid
 * Date: December 26, 2014
 * The Startup classes for this example are called during startup, in the following order:
 *  preInitCommon
 *  preInitClientOnly
 *  initCommon
 *  initClientOnly
 *  postInitCommon
 *  postInitClientOnly
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static ItemHUDactivator itemHUDactivator;  // this holds the unique instance of your item

  public static void preInitCommon()
  {
	  System.out.println("hud_overlay.preInitCommon called");
    // each instance of your item should have a name that is unique within your mod.  use lower case.
    itemHUDactivator = (ItemHUDactivator)(new ItemHUDactivator().setUnlocalizedName("mbe40_hud_overlay_item"));
	//itemHUDactivator = (ItemHUDactivator)(new ItemHUDactivator().setUnlocalizedName("test_item"));
    GameRegistry.registerItem(itemHUDactivator, "mbe40_hud_overlay_item");
	//GameRegistry.registerItem(itemHUDactivator, "test_item");
  }

  public static void initCommon()
  {
  }

  public static void postInitCommon()
  {
  }
}

