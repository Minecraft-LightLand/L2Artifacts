package dev.xkmc.l2artifacts.content.search.main;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FilteredMenuScreen extends AbstractScrollerScreen<FilteredMenu> {

	public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, ArtifactLang.TAB_FILTERED.get(), ArtifactTabRegistry.FILTERED.get());
	}

	@Override
	public Component getTitle() {
		return super.getTitle().copy().append(": " + menu.current_count.get() + "/" + menu.total_count.get()).withStyle(ChatFormatting.GRAY);
	}

}
