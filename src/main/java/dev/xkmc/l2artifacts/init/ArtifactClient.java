package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.client.tab.TabSetEffects;
import dev.xkmc.l2artifacts.content.client.tooltip.ClientItemTooltip;
import dev.xkmc.l2artifacts.content.client.tooltip.ItemTooltip;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapOverlay;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Artifacts.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ArtifactClient {

	public static TabToken<TabSetEffects> TAB_SET_EFFECTS;

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_SET_EFFECTS = TabRegistry.registerTab(3000, TabSetEffects::new, ArtifactItems.SELECT::get,
					Component.translatable("menu.tabs.set_effects"));
		});
	}

	@SubscribeEvent
	public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ItemTooltip.class, ClientItemTooltip::new);
	}

	@SubscribeEvent
	public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
	}

	@SubscribeEvent
	public static void registerOverlay(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "artifact_swap", ArtifactSwapOverlay.INSTANCE);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
	}

}
