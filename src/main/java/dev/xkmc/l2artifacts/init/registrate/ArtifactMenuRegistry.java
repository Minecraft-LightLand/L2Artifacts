package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenuScreen;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenuScreen;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenuScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenuScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class ArtifactMenuRegistry {

	public static final MenuEntry<FilteredMenu> MT_FILTER = REGISTRATE.menu("filtered",
					FilteredMenu::fromNetwork,
					() -> FilteredMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static final MenuEntry<RecycleMenu> MT_RECYCLE = REGISTRATE.menu("recycle",
					RecycleMenu::fromNetwork,
					() -> RecycleMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static final MenuEntry<UpgradeMenu> MT_UPGRADE = REGISTRATE.menu("upgrade",
					UpgradeMenu::fromNetwork,
					() -> UpgradeMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static final MenuEntry<DissolveMenu> MT_DISSOLVE = REGISTRATE.menu("dissolve",
					DissolveMenu::fromNetwork,
					() -> DissolveMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static final MenuEntry<AugmentMenu> MT_AUGMENT = REGISTRATE.menu("augment",
					AugmentMenu::fromNetwork,
					() -> AugmentMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = ForgeRegistries.MENU_TYPES.getKey(menu);
		assert rl != null;
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

	public static void register() {

	}

}
