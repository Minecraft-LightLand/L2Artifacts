package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.client.select.SetSelectScreen;
import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.client.Minecraft;

public class ArtifactClientHandler {

	public static void openSelectionScreen() {
		Minecraft.getInstance().setScreen(new SetSelectScreen());
	}

	public static void onTabSwitch(TabManager<ArtifactTabData> tab) {
		if (tab.screen instanceof StackedScreen s) {
			s.onSwitch();
		}
	}
}
