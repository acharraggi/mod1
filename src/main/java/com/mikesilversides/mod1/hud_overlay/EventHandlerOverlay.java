package com.mikesilversides.mod1.hud_overlay;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mikesilversides.mod1.Reference;
import com.mikesilversides.mod1.airstrike.StartupCommon;
import com.mikesilversides.mod1.airstrike.AirstrikeMessageToServer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Nephroid
 *
 * This class contains the code that modifies how the default overlay is drawn:
 * 1) the health hearts and armour hearts are replaced with a health / armour status bar
 * 2) the hotbar colour is changed
 *
 * In order to change the position of the existing overlay, we have to use events.
 * For a detailed documentation on how events work, see Jabelar's tutorial:
 *
 *   http://jabelarminecraft.blogspot.com/p/minecraft-forge-172-event-handling.html
 *
 * Though the tutorial is labeled 1.7.2/1.7.10, its concepts carry over to 1.8.
 */
public class EventHandlerOverlay
{
	//private final static ResourceLocation mikesOverlay = new ResourceLocation(Reference.MOD_ID,
	//        "/textures/gui/mikes_overlay.png");
	/* This object draws text using the Minecraft font */
    //FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	//private Boolean alreadyDone = false;
	private long startTime = 0;
	private static final long waitTime = 25000;   // in ms
	
	private String viewerName = null;
//	private long startNameTime = 0;
//	private static final long waitNameTime = 8000;   // in ms
	
	
  public EventHandlerOverlay(StatusBarRenderer i_HUDrenderer)
  {
    statusBarRenderer = i_HUDrenderer;
  }

  private StatusBarRenderer statusBarRenderer;

    /* The RenderGameOverlayEvent.Pre event is called before each game overlay element is
   * rendered. It is called multiple times. A list of existing overlay elements can be found
   * in net.minecraftforge.client.event.RenderGameOverlayEvent.
   *
   * If you want something to be rendered under an existing vanilla element, you would render
   * it here.
   *
   * Note that you can entirely remove the vanilla rendering by cancelling the event here.
   */

  @SubscribeEvent(receiveCanceled=true)
  public void onEvent(RenderGameOverlayEvent.Pre event) {
	//System.out.println("hud_overlay.onEvent(RenderGameOverlayEvent.Pre event) called");
    EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().thePlayer;
    if (entityPlayerSP == null) return;  // just in case

    // look for the ItemHUDactivator in the hotbar.  If not present, return without changing the HUD.
    boolean foundInHotbar = false;
    final int FIRST_HOTBAR_SLOT = 0;
    final int LAST_HOTBAR_SLOT_PLUS_ONE = FIRST_HOTBAR_SLOT + entityPlayerSP.inventory.getHotbarSize();
    for (int i = FIRST_HOTBAR_SLOT; i < LAST_HOTBAR_SLOT_PLUS_ONE; ++i) {
      ItemStack slotItemStack = entityPlayerSP.inventory.getStackInSlot(i);
      if (slotItemStack != null && slotItemStack.getItem() == com.mikesilversides.mod1.hud_overlay.StartupCommon.itemHUDactivator) {
        foundInHotbar = true;
        break;
      }
    }
    
    /* Saving the current state of OpenGL so that I can restore it when I'm done */
//    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS); //Mike-temp
//    GL11.glPushMatrix();
//    GL11.glTranslatef(0, 0, 0);
//    /* This generates the string that I want to draw. */
//    String s = "Mod1 HUD test string";
//    /* Draw the shadow string */
//    /* Set the rendering color to white */
//    //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    fr.drawString(s, 2, 2, 0x5A2B00);  //0x5A2B00  0xFFFFFF
//    //GL11.glTranslatef(2, fr.FONT_HEIGHT + 2 , 0);
//    /* Set the rendering color to white */
//    //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    /* This method tells OpenGL to draw with the custom texture */
//    //Minecraft.getMinecraft().renderEngine.bindTexture(mikesOverlay);  
//    
//    GL11.glPopMatrix(); 
//    GL11.glPopAttrib(); //Mike
    

// have TNT mode always active
//    if (!foundInHotbar) {
//    	//alreadyDone = false;
//    	return;
//    }

    //if(!alreadyDone) {
    	if(startTime == 0) {
    		startTime = Minecraft.getMinecraft().getSystemTime();
    		return;
    	}
    	long currentTime = Minecraft.getMinecraft().getSystemTime();
    	if(currentTime - waitTime > startTime) {
	    	//alreadyDone = true;  // only do this once per waitTime
	    	startTime = 0;
	    	Vec3 targetLocation;
	    	
	    	//sample found at: http://jabelarminecraft.blogspot.ca/p/minecraft-forge-172-finding-block.html
	    	//movingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity.rayTrace(200, 1.0F);
//	    	MovingObjectPosition mop = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(40d, 1.0F);
//	    	if(mop != null)
//	    	{
//	    	    //int blockHitSide = mop.sideHit;
//	    		//EnumFacing blockHitSide = mop.sideHit;
//	    	    //Block blockLookingAt = worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) ;
//	    		BlockPos blockP = mop.getBlockPos();
//	    		System.out.println("looking at X: "+blockP.getX());
//	    		System.out.println("looking at Y: "+blockP.getY());
//	    		System.out.println("looking at Z: "+blockP.getZ());
//	    		
//	    	    targetLocation = new Vec3(blockP.getX()+ 0.5, blockP.getY() + 1.1, blockP.getZ() + 0.5);
//	    	}
//	    	else {
	    		final float PARTIAL_TICKS = 1.0F;
	    		//Vec3 playerLook = playerIn.getLookVec();
	    		Vec3 playerLook = Minecraft.getMinecraft().thePlayer.getLookVec();
	    	    Vec3 playerFeetPosition = Minecraft.getMinecraft().thePlayer.getPositionEyes(PARTIAL_TICKS).subtract(0, Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0);
	    	    targetLocation = playerFeetPosition.addVector(playerLook.xCoord * 2, 0, playerLook.zCoord * 2);
//	    	    final double TARGET_DISTANCE = 5.0;
//	    	    final double HEIGHT_ABOVE_FEET = 0.1;
//	    	    targetLocation = playerFeetPosition;
//	    	    targetLocation = playerFeetPosition.addVector(playerLook.xCoord * TARGET_DISTANCE, HEIGHT_ABOVE_FEET,
//	    	                                                       playerLook.zCoord * TARGET_DISTANCE);
	    	//}
    	    //Random random = new Random();
    	    AirstrikeMessageToServer.Projectile [] choices = AirstrikeMessageToServer.Projectile.values();
    	    AirstrikeMessageToServer.Projectile projectile = choices[2];  //0=pig, 1=snowball, 2=PrimedTNT, 4=egg

    	    // following commented out to stop the TNT drops from spanky
//    	    AirstrikeMessageToServer airstrikeMessageToServer = new AirstrikeMessageToServer(projectile, targetLocation);
//    	    com.mikesilversides.mod1.airstrike.StartupCommon.simpleNetworkWrapper.sendToServer(airstrikeMessageToServer);
//    	    Minecraft.getMinecraft().thePlayer.sendChatMessage("This TNT Glitch brought to you by: Spanky");
    	}
    	
//    	if(startNameTime > 0) {
//	    	long currentNameTime = Minecraft.getMinecraft().getSystemTime();
//	    	if(currentNameTime - waitNameTime > startNameTime) {
//	    		startNameTime = 0;
//	    		viewerName = null;
//	    	}
//    	}
    //}
    
    switch (event.type) {
      case HEALTH:
        //statusBarRenderer.renderStatusBar(event.resolution.getScaledWidth(), event.resolution.getScaledHeight());        /* Call a helper method so that this method stays organized */
        /* Don't render the vanilla heart bar */
        //event.setCanceled(true);
        

        break;

      case ARMOR:
        /* Don't render the vanilla armor bar, it's part of the status bar in the HEALTH event */
        //event.setCanceled(true);
        break;

      case HOTBAR:
        /* Specify a color to render with. If you're familiar with Photoshop or something similar, this
         * basically adds a layer on top with the "Multiply" blend mode. Using the color white
         * will have no effect, and using the color black will make your texture completely black (but
         * it will preserve transparency).
         *
         * The actual arguments for glColor3f are 3 float values from 0.0f to 1.0f. These represent the
         * level of each color component using the RGB model, with 1.0f being the highest. To learn more
         * about the RGB model, visit this link:
         * http://en.wikipedia.org/wiki/RGB_color_model
         *
         * The line below turns the hotbar gold
         */
        statusBarRenderer.renderStatusBar(/*viewerName, event.resolution.getScaledWidth(), event.resolution.getScaledHeight()*/);        /* Call a helper method so that this method stays organized */
        GL11.glColor3f(1, 0.7f, 0);
        break;

      default: // If it's not one of the above cases, do nothing
        break;
    }
  }

  /* The RenderGameOverlayEvent.Post event is called after each game overlay element is rendered.
   * Similar to the RenderGameOverlayEvent.Pre event, it is called multiple times.
   * 
   * If you want something to be rendered over an existing vanilla element, you would render
   * it here.
   */
  @SubscribeEvent(receiveCanceled=true)
  public void onEvent(RenderGameOverlayEvent.Post event) {
	//System.out.println("hud_overlay.onEvent(RenderGameOverlayEvent.Post event) called");
    /* The matrix must be popped whenever it is pushed. In this example, I pushed
     * in the FOOD and AIR cases, so I have to pop in those cases here.
     * 
     */

    switch (event.type) {
      case HEALTH:
        break;
      case HOTBAR:
        /* Set the render color back to white, so that not everything appears gold. */
        GL11.glColor3f(1, 1, 1);
        break;
      default: // If it's not one of the above cases, do nothing
        break;
    }
  }
}
