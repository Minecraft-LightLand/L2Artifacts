package dev.xkmc.l2artifacts.content.client.search.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FilteredMenuScreen extends BaseContainerScreen<FilteredMenu> {

	public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(PoseStack pose, float pTick, int mx, int my) {
		SpriteManager sm = this.menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(pose);
	}

}
