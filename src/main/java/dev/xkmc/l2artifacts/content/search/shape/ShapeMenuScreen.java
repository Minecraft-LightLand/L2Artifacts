package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

public class ShapeMenuScreen extends BaseContainerScreen<ShapeMenu> implements ITabScreen {

	public ShapeMenuScreen(ShapeMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, LangData.TAB_SHAPE.get());
	}

	@Override
	protected final void init() {
		super.init();
		new FilterTabManager(this, menu.token).init(this::addRenderableWidget, FilterTabManager.SHAPE);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
		var sr = menu.sprite.get().getRenderer(this);
		sr.start(g);
		drawDisable(sr, g, ShapeSlots.BOOST_MAIN, 0, "altas_boost_main");
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, g, ShapeSlots.ARTIFACT_SUB, i, null);
		}
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, g, ShapeSlots.STAT_SUB, i, "altas_stat_container");
		}
		for (int i = 0; i < 4; i++) {
			drawDisable(sr, g, ShapeSlots.BOOST_SUB, i, "altas_boost_sub");
		}
	}

	public void drawDisable(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, ShapeSlots slot, int i, @Nullable String altas) {
		if (slot.get(menu, i).isInputLocked()) {
			sr.draw(g, slot.slot(), "toggle_slot_2", -1 + i * 18, -1);
		}
		if (altas != null && slot.get(menu, i).getItem().isEmpty()) {
			sr.draw(g, slot.slot(), altas, i * 18, 0);
		}
	}

}
