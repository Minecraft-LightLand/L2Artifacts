package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.init.L2Client;
import dev.xkmc.l2library.menu.tabs.contents.AttributeScreen;
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
		event.enqueueWork(() -> {
			AttributeScreen.LIST.add(new AttributeScreen.AttributeEntry(ArtifactRegistry.CRIT_RATE, true));
			AttributeScreen.LIST.add(new AttributeScreen.AttributeEntry(ArtifactRegistry.CRIT_DMG, true));
		});
	}

}
