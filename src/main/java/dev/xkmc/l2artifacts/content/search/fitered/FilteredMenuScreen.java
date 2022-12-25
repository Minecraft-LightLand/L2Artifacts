package dev.xkmc.l2artifacts.content.search.fitered;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.scroller.ScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FilteredMenuScreen extends AbstractScrollerScreen<FilteredMenu> {

	public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title, FilterTabManager.FILTERED);
	}

}
