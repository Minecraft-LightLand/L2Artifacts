package dev.xkmc.l2artifacts.content.search.recycle;

import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.filter.FilterScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2core.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2core.base.menu.stacked.StackedRenderHandle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class RecycleMenuScreen extends AbstractScrollerScreen<RecycleMenu> {

	private boolean pressed, canDrag, dragging, enable, hover_a, hover_b;

	public RecycleMenuScreen(RecycleMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, ArtifactLang.TAB_RECYCLE.get().withStyle(ChatFormatting.GRAY), FilterTabManager.RECYCLE);
	}

	@Override
	protected void renderBgExtra(GuiGraphics g, MenuLayoutConfig.ScreenRenderer sr, int mx, int my) {
		var spr = menu.getLayout();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (isSelected(i * 6 + j)) {
					sr.draw(g, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
				}
			}
		}
		var rect = spr.getComp("output");
		if (isHovering(rect.x, rect.y, rect.w, rect.h, mx, my)) {
			sr.draw(g, "output", "delete_on");
		}
		g.pose().pushPose();
		g.pose().translate(leftPos, topPos, 0);
		int btn_x = titleLabelX + font.width(getTitle()) + 3;
		int btn_y = titleLabelY;
		boolean h1 = isHovering(btn_x, btn_y, 8, 8, mx, my);
		boolean p1 = pressed && h1;
		var r = spr.getSide(p1 ? "button_1" : "button_1p");
		var tex = MenuLayoutConfig.getTexture(sr.id);
		g.blit(tex, btn_x, btn_y, r.x, r.y, r.w, r.h);
		if (h1) {
			FilterScreen.renderHighlight(g, btn_x, btn_y, 8, 8, -2130706433);
		}
		btn_x += r.w + 3;
		boolean h2 = isHovering(btn_x, btn_y, 8, 8, mx, my);
		boolean p2 = pressed && h2;
		r = spr.getSide(p2 ? "button_2" : "button_2p");
		g.blit(tex, btn_x, btn_y, r.x, r.y, r.w, r.h);
		if (h2) {
			FilterScreen.renderHighlight(g, btn_x, btn_y, 8, 8, -2130706433);
		}
		hover_a = h1;
		hover_b = h2;
		g.pose().popPose();
	}

	private boolean isSelected(int ind) {
		return menu.sel.get(ind);
	}

	private void forceSelect(int ind) {
		menu.sel.set(!menu.sel.get(ind), ind);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		var r = menu.getLayout().getComp("grid");
		pressed = true;
		int x = r.x + getGuiLeft();
		int y = r.y + getGuiTop();
		if (mx >= x && my >= y && mx < x + r.w * r.rx && my < y + r.h * r.ry) {
			Slot slot = getSlotUnderMouse();
			if (slot != null && slot.getContainerSlot() >= menu.extra) {
				enable = isSelected(slot.getContainerSlot() - menu.extra);
				canDrag = true;
			}
		}
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	protected boolean click(int btn) {
		if (btn >= 2 && btn < 38) {
			forceSelect(btn - 2);
		}
		return super.click(btn);
	}

	@Override
	public boolean mouseReleased(double mx, double my, int pButton) {
		dragging = false;
		canDrag = false;
		pressed = false;
		var rect = menu.getLayout().getComp("output");
		if (isHovering(rect.x, rect.y, rect.w, rect.h, mx, my)) {
			click(50);
		}
		if (hover_a) {
			click(51);
			hover_a = false;
		}
		if (hover_b) {
			click(52);
			hover_b = false;
		}
		return super.mouseReleased(mx, my, pButton);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
		var r = menu.getLayout().getComp("grid");
		int x = r.x + getGuiLeft();
		int y = r.y + getGuiTop();
		if (mx >= x && my >= y && mx < x + r.w * r.rx && my < y + r.h * r.ry) {
			Slot slot = getSlotUnderMouse();
			if (canDrag && slot != null && slot.getContainerSlot() >= menu.extra) {
				int ind = slot.getContainerSlot() - menu.extra;
				boolean selected = isSelected(ind);
				dragging = true;
				if (selected == enable) {
					click(ind + 2);
					return true;
				}
			}
		}
		return super.mouseDragged(mx, my, btn, dx, dy);
	}

	@Override
	protected void renderTooltip(GuiGraphics pPoseStack, int pX, int pY) {
		pPoseStack.pose().pushPose();
		pPoseStack.pose().translate(0, topPos, 0);
		var handle = new StackedRenderHandle(this, pPoseStack, 8, 0xFFFFFFFF, getRenderer());
		handle.drawText(ArtifactLang.TAB_INFO_TOTAL.get(menu.total_count.get()), false);
		handle.drawText(ArtifactLang.TAB_INFO_MATCHED.get(menu.current_count.get()), false);
		handle.drawText(ArtifactLang.TAB_INFO_EXP.get(formatNumber(menu.experience.get())), false);
		handle.drawText(ArtifactLang.TAB_INFO_SELECTED.get(menu.select_count.get()), false);
		handle.drawText(ArtifactLang.TAB_INFO_EXP_GAIN.get(formatNumber(menu.to_gain.get())), false);
		handle.flushText();
		pPoseStack.pose().popPose();
		if (!dragging) {
			super.renderTooltip(pPoseStack, pX, pY);
		}
	}

	private static final String[] SUFFIX = {"", "k", "M", "G", "T"};

	public static String formatNumber(int number) {
		int level = 0;
		while (true) {
			if (number < 1000 || level == SUFFIX.length - 1) {
				return number + SUFFIX[level];
			}
			if (number < 10000) {
				int a = (int) Math.round(number * 1e-2);
				int b = a / 10;
				int c = a % 10;
				return b + "." + c + SUFFIX[level + 1];
			}
			number = (int) Math.round(number * 1e-3);
			level++;
		}
	}

}