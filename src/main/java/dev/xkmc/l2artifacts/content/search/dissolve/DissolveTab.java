package dev.xkmc.l2artifacts.content.search.dissolve;

import dev.xkmc.l2artifacts.content.search.tabs.FilterGroup;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DissolveTab extends TabBase<FilterGroup, DissolveTab> {

	public DissolveTab(int index, TabToken<FilterGroup, DissolveTab> token, TabManager<FilterGroup> manager,
					   ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		NetworkManager.HANDLER.toServer(new SetFilterToServer(manager.token.token, SetFilterToServer.Type.DISSOLVE));
	}
}
