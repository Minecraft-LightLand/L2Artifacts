package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.client.search.screen.FilteredMenu;
import dev.xkmc.l2artifacts.content.client.search.screen.FilteredMenuScreen;
import dev.xkmc.l2library.repack.registrate.util.entry.MenuEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class ArtifactMenuRegistry {

	public static final MenuEntry<FilteredMenu> MT_FILTER = REGISTRATE.menu("filtered",
					FilteredMenu::fromNetwork,
					() -> FilteredMenuScreen::new)
			.lang(ArtifactMenuRegistry::getLangKey).register();

	public static String getLangKey(MenuType<?> menu) {
		ResourceLocation rl = ForgeRegistries.MENU_TYPES.getKey(menu);
		assert rl != null;
		return "container." + rl.getNamespace() + "." + rl.getPath();
	}

	public static void register() {

	}

}
