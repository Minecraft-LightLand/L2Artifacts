package dev.xkmc.l2artifacts.content.search.tabs;

import dev.xkmc.l2artifacts.content.search.augment.AugmentTab;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveTab;
import dev.xkmc.l2artifacts.content.search.filter.FilterTab;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredTab;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleTab;
import dev.xkmc.l2artifacts.content.search.shape.ShapeTab;
import dev.xkmc.l2artifacts.content.search.sort.SortTab;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeTab;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItemRegistry;
import dev.xkmc.l2tabs.tabs.core.*;
import net.minecraft.world.item.Items;

public class FilterTabManager extends TabManager<FilterGroup> {

	public static final TabGroup<FilterGroup> GROUP = new TabGroup<>(TabType.RIGHT);

	public static final TabToken<FilterGroup, FilteredTab> FILTERED = GROUP.registerTab(1000, FilteredTab::new, () -> Items.CHEST, LangData.TAB_FILTERED.get());
	public static final TabToken<FilterGroup, FilterTab> FILTER = GROUP.registerTab(2000, FilterTab::new, () -> Items.HOPPER, LangData.TAB_FILTER.get());
	public static final TabToken<FilterGroup, SortTab> SORT = GROUP.registerTab(3000, SortTab::new, () -> Items.COMPARATOR, LangData.TAB_SORT.get());
	public static final TabToken<FilterGroup, RecycleTab> RECYCLE = GROUP.registerTab(4000, RecycleTab::new, () -> Items.COMPOSTER, LangData.TAB_RECYCLE.get());
	public static final TabToken<FilterGroup, UpgradeTab> UPGRADE = GROUP.registerTab(5000, UpgradeTab::new, () -> Items.ANVIL, LangData.TAB_UPGRADE.get());
	public static final TabToken<FilterGroup, DissolveTab> DISSOLVE = GROUP.registerTab(6000, DissolveTab::new, () -> ArtifactItemRegistry.ITEM_STAT[4].get(), LangData.TAB_DISSOLVE.get());

	public static final TabToken<FilterGroup, AugmentTab> AUGMENT = GROUP.registerTab(7000, AugmentTab::new, () -> ArtifactItemRegistry.ITEM_BOOST_MAIN[4].get(), LangData.TAB_AUGMENT.get());
	public static final TabToken<FilterGroup, ShapeTab> SHAPE = GROUP.registerTab(8000, ShapeTab::new, ArtifactItemRegistry.SELECT::get, LangData.TAB_SHAPE.get());

	public FilterTabManager(ITabScreen scr, ArtifactChestToken token) {
		super(scr, new FilterGroup(GROUP, token));
	}

}
