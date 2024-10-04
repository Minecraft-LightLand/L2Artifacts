package dev.xkmc.l2artifacts.content.search.genesis;

import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.content.search.common.OpenTabToServer;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;

public class ShapeTab extends TabBase<ArtifactTabData, ShapeTab> {

	public ShapeTab(int index, TabToken<ArtifactTabData, ShapeTab> token, TabManager<ArtifactTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		L2Artifacts.HANDLER.toServer(OpenTabToServer.of(manager.token, OpenTabToServer.Type.SHAPE));
	}

}
