package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.l2artifacts.content.client.tab.TabSetEffects;
import dev.xkmc.l2artifacts.content.search.convert.DissolveTab;
import dev.xkmc.l2artifacts.content.search.convert.RecycleTab;
import dev.xkmc.l2artifacts.content.search.genesis.ShapeTab;
import dev.xkmc.l2artifacts.content.search.main.FilteredTab;
import dev.xkmc.l2artifacts.content.search.sort.FilterTab;
import dev.xkmc.l2artifacts.content.search.sort.SortTab;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabGroup;
import dev.xkmc.l2artifacts.content.search.upgrade.AugmentTab;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeTab;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.core.TabType;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

public class ArtifactTabRegistry {

	private static final ResourceLocation DUMMY = L2Tabs.loc(L2Artifacts.MODID);

	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(L2Artifacts.REG, L2Tabs.TABS.key());

	public static Val<TabToken<InvTabData, TabSetEffects>> TAB_SET_EFFECTS = TAB_REG.reg("set_effects",
			() -> L2Tabs.GROUP.registerTab(() -> TabSetEffects::new,
					Component.translatable("menu.tabs.set_effects")));

	public static final TabGroup<ArtifactTabData> ARTIFACT = new ArtifactTabGroup(TabType.RIGHT, 8, false);

	public static final Val<TabToken<ArtifactTabData, FilteredTab>> FILTERED = TAB_REG.reg("main", () -> ARTIFACT.registerTab(() -> FilteredTab::new, ArtifactLang.TAB_FILTERED.get()));
	public static final Val<TabToken<ArtifactTabData, FilterTab>> FILTER = TAB_REG.reg("filter", () -> ARTIFACT.registerTab(() -> FilterTab::new, ArtifactLang.TAB_FILTER.get()));
	public static final Val<TabToken<ArtifactTabData, SortTab>> SORT = TAB_REG.reg("sort", () -> ARTIFACT.registerTab(() -> SortTab::new, ArtifactLang.TAB_SORT.get()));
	public static final Val<TabToken<ArtifactTabData, RecycleTab>> RECYCLE = TAB_REG.reg("recycle", () -> ARTIFACT.registerTab(() -> RecycleTab::new, ArtifactLang.TAB_RECYCLE.get()));
	public static final Val<TabToken<ArtifactTabData, UpgradeTab>> UPGRADE = TAB_REG.reg("upgrade", () -> ARTIFACT.registerTab(() -> UpgradeTab::new, ArtifactLang.TAB_UPGRADE.get()));
	public static final Val<TabToken<ArtifactTabData, DissolveTab>> DISSOLVE = TAB_REG.reg("dissolve", () -> ARTIFACT.registerTab(() -> DissolveTab::new, ArtifactLang.TAB_DISSOLVE.get()));
	public static final Val<TabToken<ArtifactTabData, AugmentTab>> AUGMENT = TAB_REG.reg("augment", () -> ARTIFACT.registerTab(() -> AugmentTab::new, ArtifactLang.TAB_AUGMENT.get()));
	public static final Val<TabToken<ArtifactTabData, ShapeTab>> SHAPE = TAB_REG.reg("genesis", () -> ARTIFACT.registerTab(() -> ShapeTab::new, ArtifactLang.TAB_SHAPE.get()));

	public static final List<Val<? extends TabToken<ArtifactTabData, ?>>> LIST_0 = List.of(FILTERED, FILTER, SORT, RECYCLE, UPGRADE);
	public static final List<Val<? extends TabToken<ArtifactTabData, ?>>> LIST_1 = List.of(FILTERED, FILTER, SORT, RECYCLE, DISSOLVE, AUGMENT, SHAPE);

	public static void register() {

	}

	public static void genTabs(RegistrateDataMapProvider pvd) {
		var icon = pvd.builder(L2Tabs.ICON.reg());
		icon.add(FILTERED.id(), Items.CHEST, false);
		icon.add(FILTER.id(), Items.HOPPER, false);
		icon.add(SORT.id(), Items.COMPARATOR, false);
		icon.add(RECYCLE.id(), Items.COMPOSTER, false);
		icon.add(UPGRADE.id(), Items.ANVIL, false);
		icon.add(DISSOLVE.id(), ArtifactItems.ITEM_STAT[4].get(), false);
		icon.add(AUGMENT.id(), ArtifactItems.ITEM_BOOST_MAIN[4].get(), false);
		icon.add(SHAPE.id(), ArtifactItems.SELECT.get(), false);
		icon.add(TAB_SET_EFFECTS.id(), ArtifactItems.SELECT.get(), false);

		var order = pvd.builder(L2Tabs.ORDER.reg());
		order.add(FILTERED.id(), 0, false);
		order.add(FILTER.id(), 1, false);
		order.add(SORT.id(), 2, false);
		order.add(RECYCLE.id(), 3, false);
		order.add(UPGRADE.id(), 4, false);
		order.add(DISSOLVE.id(), 5, false);
		order.add(AUGMENT.id(), 6, false);
		order.add(SHAPE.id(), 7, false);
		order.add(TAB_SET_EFFECTS.id(), 3000, false);

	}
}
