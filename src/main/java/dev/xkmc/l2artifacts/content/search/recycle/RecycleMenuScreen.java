package dev.xkmc.l2artifacts.content.search.recycle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RecycleMenuScreen extends AbstractScrollerScreen<RecycleMenu> {

	public RecycleMenuScreen(RecycleMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title, FilterTabManager.RECYCLE);
	}

	@Override
	protected void renderBgExtra(PoseStack pose, SpriteManager.ScreenRenderer sr) {

	}

}