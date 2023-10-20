package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterGroup;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FilterTab extends TabBase<FilterGroup, FilterTab> {

	public FilterTab(int index, TabToken<FilterGroup, FilterTab> token, TabManager<FilterGroup> manager,
					 ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		if (manager.getScreen() instanceof StackedScreen stacked) {
			stacked.onSwitch();
		}
		var token = ArtifactChestToken.of(Proxy.getClientPlayer(), manager.token.token.invSlot);
		Minecraft.getInstance().setScreen(new FilterScreen(token));
		NetworkManager.HANDLER.toServer(new SetFilterToServer(manager.token.token, null));
	}
}
