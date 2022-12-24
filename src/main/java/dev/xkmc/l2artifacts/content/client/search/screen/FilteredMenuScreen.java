package dev.xkmc.l2artifacts.content.client.search.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.client.search.scroller.Scroller;
import dev.xkmc.l2artifacts.content.client.search.scroller.ScrollerScreen;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class FilteredMenuScreen extends BaseContainerScreen<FilteredMenu> implements ScrollerScreen {

	private final Scroller scroller;

	public FilteredMenuScreen(FilteredMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
		scroller = new Scroller(this, cont.sprite, "slider_middle", "slider_light", "slider_dark");
	}

	@Override
	protected void renderBg(PoseStack pose, float pTick, int mx, int my) {
		SpriteManager sm = this.menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(pose);
		scroller.render(pose, sr);
	}

	@Override
	public void scrollTo(int i) {
		if (i < menu.getScroll()) {
			click(0);
		} else if (i > menu.getScroll()) {
			click(1);
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		SpriteManager.Rect r = menu.sprite.getComp("grid");
		int x = r.x + getGuiLeft();
		int y = r.y + getGuiTop();
		if (mx >= x && my >= y && mx < x + r.w * r.rx && my < y + r.h * r.ry) {
			Slot slot = getSlotUnderMouse();
			if (slot != null && slot.getContainerSlot() >= 2) {
				click(slot.getContainerSlot());
			}
		}
		return scroller.mouseClicked(mx, my, btn) || super.mouseClicked(mx, my, btn);
	}

	@Override
	public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
		return scroller.mouseDragged(mx, my, btn, dx, dy) || super.mouseDragged(mx, my, btn, dx, dy);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double d) {
		return scroller.mouseScrolled(mx, my, d) || super.mouseScrolled(mx, my, d);
	}

}
