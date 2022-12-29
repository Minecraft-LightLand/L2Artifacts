package dev.xkmc.l2artifacts.content.search.recycle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.filter.FilterScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class RecycleMenuScreen extends AbstractScrollerScreen<RecycleMenu> {

	private boolean pressed, canDrag, dragging, enable, hover_a, hover_b;

	public RecycleMenuScreen(RecycleMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title, FilterTabManager.RECYCLE);
	}

	@Override
	protected void renderBgExtra(PoseStack pose, SpriteManager.ScreenRenderer sr, int mx, int my) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (isSelected(i * 6 + j)) {
					sr.draw(pose, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
				}
			}
		}
		var rect = menu.sprite.getComp("output");
		if (isHovering(rect.x, rect.y, rect.w, rect.h, mx, my)) {
			sr.draw(pose, "output", "delete_on");
		}
		pose.pushPose();
		pose.translate(leftPos, topPos, 0);
		int btn_x = titleLabelX + font.width(getTitle()) + 3;
		int btn_y = titleLabelY;
		boolean h1 = isHovering(btn_x, btn_y, 8, 8, mx, my);
		boolean p1 = pressed && h1;
		SpriteManager.Rect r = menu.sprite.getSide(p1 ? "button_1" : "button_1p");
		blit(pose, btn_x, btn_y, r.x, r.y, r.w, r.h);
		if (h1) {
			FilterScreen.renderHighlight(pose, btn_x, btn_y, 8, 8, getBlitOffset(), -2130706433);
		}
		btn_x += r.w + 3;
		boolean h2 = isHovering(btn_x, btn_y, 8, 8, mx, my);
		boolean p2 = pressed && h2;
		r = menu.sprite.getSide(p2 ? "button_2" : "button_2p");
		blit(pose, btn_x, btn_y, r.x, r.y, r.w, r.h);
		if (h2) {
			FilterScreen.renderHighlight(pose, btn_x, btn_y, 8, 8, getBlitOffset(), -2130706433);
		}
		hover_a = h1;
		hover_b = h2;
		pose.popPose();
	}

	private boolean isSelected(int ind) {
		return ((ind < 18 ? menu.sel_0.get() >> ind : menu.sel_1.get() >> (ind - 18)) & 1) != 0;
	}

	private void forceSelect(int ind) {
		if (ind < 18) menu.sel_0.set(menu.sel_0.get() ^ (1 << ind));
		else menu.sel_1.set(menu.sel_1.get() ^ (1 << (ind - 18)));
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		SpriteManager.Rect r = menu.sprite.getComp("grid");
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
		var rect = menu.sprite.getComp("output");
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
		SpriteManager.Rect r = menu.sprite.getComp("grid");
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
	protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
		pPoseStack.pushPose();
		pPoseStack.translate(0, topPos, 0);
		var handle = new StackedRenderHandle(this, pPoseStack, 8, 0xFFFFFFFF, menu.sprite);
		handle.drawText(LangData.TAB_INFO_TOTAL.get(menu.total_count.get()));
		handle.drawText(LangData.TAB_INFO_MATCHED.get(menu.current_count.get()));
		handle.drawText(LangData.TAB_INFO_EXP.get(formatNumber(menu.experience.get())));
		handle.drawText(LangData.TAB_INFO_SELECTED.get(menu.select_count.get()));
		handle.drawText(LangData.TAB_INFO_EXP_GAIN.get(formatNumber(menu.to_gain.get())));
		handle.flushText();
		pPoseStack.popPose();
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