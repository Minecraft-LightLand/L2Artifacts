package dev.xkmc.l2artifacts.compat;

import dev.xkmc.l2artifacts.content.search.augment.AugmentMenuScreen;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenuScreen;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenuScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenuScreen;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.gui.GuiProperties;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@JeiPlugin
public class ArtifactJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = new ResourceLocation(L2Artifacts.MODID, "main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addGuiScreenHandler(FilteredMenuScreen.class, ArtifactJEIPlugin::create);
		registration.addGuiScreenHandler(RecycleMenuScreen.class, e -> null);
		registration.addGuiScreenHandler(UpgradeMenuScreen.class, ArtifactJEIPlugin::create);
		registration.addGuiScreenHandler(DissolveMenuScreen.class, ArtifactJEIPlugin::create);
		registration.addGuiScreenHandler(AugmentMenuScreen.class, ArtifactJEIPlugin::create);
	}


	@Nullable
	public static GuiProperties create(IFilterScreen screen) {
		if (screen.screenWidth() <= 0 || screen.screenHeight() <= 0) {
			return null;
		}
		int x = screen.getGuiLeft();
		int y = screen.getGuiTop();
		int width = screen.getXSize() + 32;
		int height = screen.getYSize();
		if (width <= 0 || height <= 0) {
			return null;
		}
		return new GuiProperties(screen.asScreen().getClass(),
				x, y, width, height,
				screen.screenWidth(), screen.screenHeight()
		);
	}


}
