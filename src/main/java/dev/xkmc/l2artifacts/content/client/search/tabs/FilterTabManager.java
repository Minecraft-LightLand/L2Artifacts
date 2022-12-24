package dev.xkmc.l2artifacts.content.client.search.tabs;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.client.search.filter.FilterTab;
import dev.xkmc.l2artifacts.content.client.search.fitered.FilteredTab;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.data.LangData;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FilterTabManager {

	public static final FilterTabToken<FilteredTab> FILTERED = new FilterTabToken<>(0, FilteredTab::new, () -> Items.CHEST, LangData.TAB_FILTERED.get());
	public static final FilterTabToken<FilterTab> FILTER = new FilterTabToken<>(1, FilterTab::new, () -> Items.HOPPER, LangData.TAB_FILTER.get());

	public static final List<FilterTabToken<?>> LIST = List.of(FILTERED, FILTER);

	private final List<FilterTabBase<?>> list = new ArrayList<>();
	private final IFilterScreen screen;

	public final ArtifactChestToken token;

	public int tabPage, maxPages;
	public FilterTabToken<?> selected;

	public FilterTabManager(IFilterScreen screen, ArtifactChestToken token) {
		this.screen = screen;
		this.token = token;
	}

	public void init(Consumer<AbstractWidget> adder, FilterTabToken<?> selected) {
		list.clear();
		this.selected = selected;
		int guiLeft = screen.getGuiLeft();
		int guiTop = screen.getGuiTop();
		for (FilterTabToken<?> token : LIST) {
			FilterTabBase<?> tab = token.create(this);
			tab.x = guiLeft + FilterTabType.LEFT.getX(token.index);
			tab.y = guiTop+ FilterTabType.LEFT.getY(token.index);
			adder.accept(tab);
			list.add(tab);
		}
		updateVisibility();
	}

	private void updateVisibility() {
		for (FilterTabBase<?> tab : list) {
			tab.visible = tab.token.index >= tabPage * FilterTabType.MAX_TABS && tab.token.index < (tabPage + 1) * FilterTabType.MAX_TABS;
			tab.active = tab.visible;
		}
	}

	public Screen getScreen() {
		return screen.asScreen();
	}

	public void onToolTipRender(PoseStack stack, int mouseX, int mouseY) {
		for (FilterTabBase<?> tab : list) {
			if (tab.visible && tab.isHoveredOrFocused()) {
				tab.onTooltip(stack, mouseX, mouseY);
			}
		}
	}
}
