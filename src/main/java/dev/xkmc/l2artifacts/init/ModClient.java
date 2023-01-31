package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.client.tab.TabSetEffects;
import dev.xkmc.l2artifacts.content.client.tooltip.ClientItemTooltip;
import dev.xkmc.l2artifacts.content.client.tooltip.ItemTooltip;
import dev.xkmc.l2artifacts.init.registrate.items.LAItem0;
import dev.xkmc.l2library.base.tabs.core.TabRegistry;
import dev.xkmc.l2library.base.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Artifacts.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClient {

	public static TabToken<TabSetEffects> TAB_SET_EFFECTS;

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TAB_SET_EFFECTS = TabRegistry.registerTab(TabSetEffects::new, () -> LAItem0.SET_GAMBLER.items[0][0].get(),
					Component.translatable("menu.tabs.set_effects"));
		});
	}

	@SubscribeEvent
	public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ItemTooltip.class, ClientItemTooltip::new);
	}

}
