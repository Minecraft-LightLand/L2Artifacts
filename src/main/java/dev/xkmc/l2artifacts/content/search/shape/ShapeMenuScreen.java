package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
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

	@Override
	protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
		p_281635_.drawString(this.font, this.title.copy().withStyle(ChatFormatting.GRAY), this.titleLabelX, this.titleLabelY, 4210752, false);
		p_281635_.drawString(this.font, this.playerInventoryTitle.copy().withStyle(ChatFormatting.GRAY), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}

	public void drawDisable(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, ShapeSlots slot, int i, @Nullable String altas) {
		if (slot.get(menu, i).isInputLocked()) {
			sr.draw(g, slot.slot(), "toggle_slot_2", -1 + i * 18, -1);
		} else {
			sr.draw(g, slot.slot(), "toggle_slot_0", -1 + i * 18, -1);
		}
		if (altas != null && slot.get(menu, i).getItem().isEmpty()) {
			sr.draw(g, slot.slot(), altas, i * 18, 0);
		}
	}

}
