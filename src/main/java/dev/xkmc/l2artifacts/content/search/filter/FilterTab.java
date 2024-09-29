package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabBase;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FilterTab extends FilterTabBase<FilterTab> {

	public FilterTab(int index, FilterTabToken<FilterTab> token, FilterTabManager manager, ItemStack stack, Component title) {
		super(index, token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		manager.screen.onSwitch();
		var token = ArtifactChestToken.of(Proxy.getClientPlayer(), manager.token.invSlot);
		Minecraft.getInstance().setScreen(new FilterScreen(token));
		NetworkManager.HANDLER.toServer(SetFilterToServer.of(manager.token, null));
	}
}
