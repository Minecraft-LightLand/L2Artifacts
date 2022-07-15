package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.client.TabSetEffects;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2library.base.tabs.contents.TabInventory;
import dev.xkmc.l2library.base.tabs.core.TabRegistry;
import dev.xkmc.l2library.base.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModClient {

	public static TabToken<TabSetEffects> TAB_SET_EFFECTS;

	public static void onCtorClient(IEventBus bus, IEventBus eventBus) {
		bus.addListener(ModClient::clientSetup);
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ClientRegister.registerItemProperties();
		ClientRegister.registerOverlays();
		ClientRegister.registerKeys();
		event.enqueueWork(() -> {
			TAB_SET_EFFECTS = TabRegistry.registerTab(TabSetEffects::new, () -> ArtifactItemRegistry.TAB_ARTIFACT.makeIcon().getItem(),
					Component.translatable("menu.tabs.set_effects"));
		});
	}

}
