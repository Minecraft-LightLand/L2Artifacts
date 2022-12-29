package dev.xkmc.l2artifacts.content.search.sort;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabBase;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetFilterToServer;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SortTab extends FilterTabBase<SortTab> {

	public SortTab(FilterTabToken<SortTab> token, FilterTabManager manager, ItemStack stack, Component title) {
		super(token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		manager.screen.onSwitch();
		var token = ArtifactChestToken.of(Proxy.getClientPlayer(), manager.token.hand);
		Minecraft.getInstance().setScreen(new SortScreen(token));
		NetworkManager.HANDLER.toServer(new SetFilterToServer(manager.token, null));
	}
}
