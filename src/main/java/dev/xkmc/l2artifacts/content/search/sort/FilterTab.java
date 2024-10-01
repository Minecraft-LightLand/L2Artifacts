package dev.xkmc.l2artifacts.content.search.sort;

import dev.xkmc.l2artifacts.content.search.token.ArtifactTabScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FilterTab extends TabBase<ArtifactTabData, FilterTab> {

	public FilterTab(int index, TabToken<ArtifactTabData, FilterTab> token, TabManager<ArtifactTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		if (manager.screen instanceof ArtifactTabScreen scr) scr.onSwitch();
		Minecraft.getInstance().setScreen(new FilterScreen(manager.token.copy()));
		NetworkManager.HANDLER.toServer(SetFilterToServer.of(manager.token, null));
	}
}
