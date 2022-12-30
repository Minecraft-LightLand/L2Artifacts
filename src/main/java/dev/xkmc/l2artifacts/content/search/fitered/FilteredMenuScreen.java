package dev.xkmc.l2artifacts.content.search.fitered;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FilteredMenuScreen extends AbstractScrollerScreen<FilteredMenu> {

	public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, LangData.TAB_FILTERED.get(), FilterTabManager.FILTERED);
	}

	@Override
	public Component getTitle() {
		return super.getTitle().copy().append(": " + menu.current_count.get() + "/" + menu.total_count.get());
	}

}
