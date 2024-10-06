package dev.xkmc.l2artifacts.content.search.sort;

import dev.xkmc.l2artifacts.content.search.common.OpenTabToServer;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
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
		L2Artifacts.HANDLER.toServer(OpenTabToServer.of(manager, null));
		Minecraft.getInstance().setScreen(new FilterScreen(manager.token.copy()));

	}
}
