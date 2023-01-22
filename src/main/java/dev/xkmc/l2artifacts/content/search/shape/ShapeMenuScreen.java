package dev.xkmc.l2artifacts.content.search.shape;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class ShapeMenuScreen extends BaseContainerScreen<ShapeMenu> implements IFilterScreen {

	public ShapeMenuScreen(ShapeMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, LangData.TAB_SHAPE.get());
	}

	@Override
	protected final void init() {
		super.init();
		new FilterTabManager(this, menu.token).init(this::addRenderableWidget, FilterTabManager.SHAPE);
	}

	@Override
	public int screenWidth() {
		return width;
	}

	@Override
	public int screenHeight() {
		return height;
	}

	@Override
	protected void renderBg(PoseStack pose, float pTick, int mx, int my) {
		var sr = menu.sprite.getRenderer(this);
		sr.start(pose);
		drawDisable(sr, pose, ShapeSlots.BOOST_MAIN, 0, "altas_boost_main");
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, pose, ShapeSlots.ARTIFACT_SUB, i, null);
		}
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, pose, ShapeSlots.STAT_SUB, i, "altas_stat_container");
		}
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, pose, ShapeSlots.BOOST_SUB, i, "altas_boost_sub");
		}
	}

	public void drawDisable(SpriteManager.ScreenRenderer sr, PoseStack pose, ShapeSlots slot, int i, @Nullable String altas) {
		if (slot.get(menu, i).isInputLocked()) {
			sr.draw(pose, slot.slot(), "toggle_slot_2", -1 + i * 18, -1);
		}
		if (altas != null && slot.get(menu, i).getItem().isEmpty()) {
			sr.draw(pose, slot.slot(), altas, i * 18, 0);
		}
	}

}
