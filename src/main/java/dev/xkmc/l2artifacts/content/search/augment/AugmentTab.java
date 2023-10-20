package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.search.tabs.FilterGroup;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AugmentTab extends TabBase<FilterGroup, AugmentTab> {

	public AugmentTab(int index, TabToken<FilterGroup, AugmentTab> token, TabManager<FilterGroup> manager,
					  ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		NetworkManager.HANDLER.toServer(new SetFilterToServer(manager.token.token, SetFilterToServer.Type.AUGMENT));
	}
}
