package dev.xkmc.l2artifacts.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClient {

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(ModClient::clientSetup);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ClientRegister.registerItemProperties();
		ClientRegister.registerOverlays();
		ClientRegister.registerKeys();
	}

}
