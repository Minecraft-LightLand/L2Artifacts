package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2artifacts.compat.swap.ArtifactSwapMenu;
import dev.xkmc.l2artifacts.compat.swap.ArtifactSwapScreen;
import dev.xkmc.l2artifacts.content.search.convert.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.convert.DissolveMenuScreen;
import dev.xkmc.l2artifacts.content.search.convert.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.convert.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.genesis.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.genesis.ShapeMenuScreen;
import dev.xkmc.l2artifacts.content.search.main.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.main.FilteredMenuScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.upgrade.AugmentMenuScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenuScreen;
import dev.xkmc.l2backpack.init.L2Backpack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.fml.ModList;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class ArtifactMenuRegistry {

	public static final MenuEntry<FilteredMenu> MT_FILTER = REGISTRATE.menu("filtered",
					FilteredMenu::fromNetwork,
					() -> FilteredMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Pocket").register();

	public static final MenuEntry<RecycleMenu> MT_RECYCLE = REGISTRATE.menu("recycle",
					RecycleMenu::fromNetwork,
					() -> RecycleMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Exp Conversion").register();

	public static final MenuEntry<UpgradeMenu> MT_UPGRADE = REGISTRATE.menu("upgrade",
					UpgradeMenu::fromNetwork,
					() -> UpgradeMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Upgrade").register();

	public static final MenuEntry<DissolveMenu> MT_DISSOLVE = REGISTRATE.menu("dissolve",
					DissolveMenu::fromNetwork,
					() -> DissolveMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Dissolve").register();

	public static final MenuEntry<AugmentMenu> MT_AUGMENT = REGISTRATE.menu("augment",
					AugmentMenu::fromNetwork,
					() -> AugmentMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Augment").register();

	public static final MenuEntry<ShapeMenu> MT_SHAPE = REGISTRATE.menu("shape",
					ShapeMenu::fromNetwork,
					() -> ShapeMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey, "Artifact Genesis").register();

	public static MenuEntry<ArtifactSwapMenu> MT_SWAP;

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = BuiltInRegistries.MENU.getKey(menu);
		assert rl != null;
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

	public static void register() {
		if (ModList.get().isLoaded(L2Backpack.MODID)) {
			MT_SWAP = REGISTRATE.menu("swap",
							ArtifactSwapMenu::fromNetwork,
							() -> ArtifactSwapScreen::new)
					.lang(ArtifactMenuRegistry::getLangKey, "Artifact Quick Swap").register();
		}

	}

}
