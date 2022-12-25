package dev.xkmc.l2artifacts.compat.jei;

import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenuScreen;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.common.gui.GuiProperties;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
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
	}


	@Nullable
	public static GuiProperties create(FilteredMenuScreen screen) {
		if (screen.width <= 0 || screen.height <= 0) {
			return null;
		}
		IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
		int x = screenHelper.getGuiLeft(screen);
		int y = screenHelper.getGuiTop(screen);
		int width = screenHelper.getXSize(screen);
		int height = screenHelper.getYSize(screen);
		if (width <= 0 || height <= 0) {
			return null;
		}
		return new GuiProperties(screen.getClass(),
				x, y, width + 32, height,
				screen.width, screen.height
		);
	}


}
