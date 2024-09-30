package dev.xkmc.l2artifacts.compat;

import dev.xkmc.l2artifacts.content.search.augment.AugmentMenuScreen;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenuScreen;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenuScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenuScreen;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenuScreen;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2tabs.compat.jei.SideTabProperties;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ArtifactJEIPlugin implements IModPlugin {

	public static final ResourceLocation ID = L2Artifacts.loc("main");

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration r) {
		var prop = new SideTabProperties(ArtifactTabRegistry.ARTIFACT);
		prop.register(r,
				FilteredMenuScreen.class,
				UpgradeMenuScreen.class,
				DissolveMenuScreen.class,
				AugmentMenuScreen.class,
				ShapeMenuScreen.class
		);
		r.addGuiScreenHandler(RecycleMenuScreen.class, e -> null);
	}

}
