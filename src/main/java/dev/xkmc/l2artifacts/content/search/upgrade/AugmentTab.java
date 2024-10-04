package dev.xkmc.l2artifacts.content.search.upgrade;

import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.content.search.common.OpenTabToServer;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;

public class AugmentTab extends TabBase<ArtifactTabData, AugmentTab> {

	public AugmentTab(int index, TabToken<ArtifactTabData, AugmentTab> token, TabManager<ArtifactTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		L2Artifacts.HANDLER.toServer(OpenTabToServer.of(manager.token, OpenTabToServer.Type.AUGMENT));
	}
}
