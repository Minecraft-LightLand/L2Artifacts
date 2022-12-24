package dev.xkmc.l2artifacts.content.client.search.filter;

import dev.xkmc.l2artifacts.content.client.search.tabs.FilterTabBase;
import dev.xkmc.l2artifacts.content.client.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.client.search.tabs.FilterTabToken;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FilterTab extends FilterTabBase<FilterTab> {

	public FilterTab(FilterTabToken<FilterTab> token, FilterTabManager manager, ItemStack stack, Component title) {
		super(token, manager, stack, title);
	}

	@Override
	public void onTabClicked() {
		Minecraft.getInstance().setScreen(new FilterScreen(manager.token));
	}
}
