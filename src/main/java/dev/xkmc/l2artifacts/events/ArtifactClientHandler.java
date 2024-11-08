package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.client.select.SetSelectScreen;
import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Artifacts.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ArtifactClientHandler {

	@SubscribeEvent
	public static void tooltipSkip(GatherSkippedAttributeTooltipsEvent event) {
		if (!(event.getStack().getItem() instanceof BaseArtifact)) return;
		var opt = BaseArtifact.getStats(event.getStack());
		if (opt.isEmpty()) return;
		var slot = opt.get().slot().getCurioIdentifier();
		var id = ResourceLocation.fromNamespaceAndPath("curios", slot);
		skip(event, opt.get(), id);
		skip(event, opt.get(), id.withSuffix("0"));
		skip(event, opt.get(), id.withSuffix("/0"));

	}

	private static void skip(GatherSkippedAttributeTooltipsEvent event, ArtifactStats stats, ResourceLocation rl) {
		Set<ResourceLocation> set = new HashSet<>();
		for (var e : stats.buildAttributes(rl).values()) {
			set.add(e.id());
		}
		for (var e : set) {
			event.skipId(e);
		}
	}

	public static void openSelectionScreen() {
		Minecraft.getInstance().setScreen(new SetSelectScreen());
	}

	public static void onTabSwitch(TabManager<ArtifactTabData> tab) {
		if (tab.screen instanceof StackedScreen s) {
			s.onSwitch();
		}
	}

}
