package com.mikesilversides.mod1.proxy;

import com.mikesilversides.mod1.init.TutorialBlocks;
import com.mikesilversides.mod1.init.TutorialItems;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerRenders(){
		TutorialBlocks.registerRenders();
		TutorialItems.registerRenders();
	}
}
