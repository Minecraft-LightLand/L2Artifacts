package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.client.tab.TabSetEffects;
import dev.xkmc.l2artifacts.content.search.upgrade.AugmentTab;
import dev.xkmc.l2artifacts.content.search.convert.DissolveTab;
import dev.xkmc.l2artifacts.content.search.sort.FilterTab;
import dev.xkmc.l2artifacts.content.search.main.FilteredTab;
import dev.xkmc.l2artifacts.content.search.convert.RecycleTab;
import dev.xkmc.l2artifacts.content.search.genesis.ShapeTab;
import dev.xkmc.l2artifacts.content.search.sort.SortTab;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeTab;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.core.TabType;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ArtifactTabRegistry {

	private static final ResourceLocation DUMMY = L2Tabs.loc(L2Artifacts.MODID);

	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(L2Artifacts.REG, L2Tabs.TABS.key());

	public static Val<TabToken<InvTabData, TabSetEffects>> TAB_SET_EFFECTS = TAB_REG.reg("set_effects",
			() -> L2Tabs.GROUP.registerTab(() -> TabSetEffects::new,
					Component.translatable("menu.tabs.set_effects")));//3000, ArtifactItems.SELECT::get,

	public static final TabGroup<ArtifactTabData> ARTIFACT = new TabGroup<>(TabType.RIGHT, 8, false);

	public static final Val<TabToken<ArtifactTabData, FilteredTab>> FILTERED = TAB_REG.reg("main", () -> ARTIFACT.registerTab(() -> FilteredTab::new, ArtifactLang.TAB_FILTERED.get()));//chest
	public static final Val<TabToken<ArtifactTabData, FilterTab>> FILTER = TAB_REG.reg("filter", () -> ARTIFACT.registerTab(() -> FilterTab::new, ArtifactLang.TAB_FILTER.get()));//hopper
	public static final Val<TabToken<ArtifactTabData, SortTab>> SORT = TAB_REG.reg("sort", () -> ARTIFACT.registerTab(() -> SortTab::new, ArtifactLang.TAB_SORT.get()));// () -> Items.COMPARATOR
	public static final Val<TabToken<ArtifactTabData, RecycleTab>> RECYCLE = TAB_REG.reg("recycle", () -> ARTIFACT.registerTab(() -> RecycleTab::new, ArtifactLang.TAB_RECYCLE.get()));// () -> Items.COMPOSTER,
	public static final Val<TabToken<ArtifactTabData, UpgradeTab>> UPGRADE = TAB_REG.reg("upgrade", () -> ARTIFACT.registerTab(() -> UpgradeTab::new, ArtifactLang.TAB_UPGRADE.get()));//() -> Items.ANVIL,
	public static final Val<TabToken<ArtifactTabData, DissolveTab>> DISSOLVE = TAB_REG.reg("dissolve", () -> ARTIFACT.registerTab(() -> DissolveTab::new, ArtifactLang.TAB_DISSOLVE.get()));// () -> ArtifactItems.ITEM_STAT[4].get(),
	public static final Val<TabToken<ArtifactTabData, AugmentTab>> AUGMENT = TAB_REG.reg("augment", () -> ARTIFACT.registerTab(() -> AugmentTab::new, ArtifactLang.TAB_AUGMENT.get()));// () -> ArtifactItems.ITEM_BOOST_MAIN[4].get(),
	public static final Val<TabToken<ArtifactTabData, ShapeTab>> SHAPE = TAB_REG.reg("genesis", () -> ARTIFACT.registerTab(() -> ShapeTab::new, ArtifactLang.TAB_SHAPE.get()));//ArtifactItems.SELECT::get,

	public static final List<Val<? extends TabToken<ArtifactTabData, ?>>> LIST_0 = List.of(FILTERED, FILTER, SORT, RECYCLE, UPGRADE);
	public static final List<Val<? extends TabToken<ArtifactTabData, ?>>> LIST_1 = List.of(FILTERED, FILTER, SORT, RECYCLE, DISSOLVE, AUGMENT, SHAPE);


}
