package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.base.menu.scroller.Scroller;
import dev.xkmc.l2library.base.menu.scroller.ScrollerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;

public class AbstractScrollerScreen<T extends AbstractScrollerMenu<T>>
		extends BaseContainerScreen<T> implements ScrollerScreen, IFilterScreen {

	private final Scroller scroller;
	private final FilterTabToken<?> tab;

	public AbstractScrollerScreen(T cont, Inventory plInv, Component title, FilterTabToken<?> tab) {
		super(cont, plInv, title);
		scroller = new Scroller(this, cont.sprite.get(),
				"slider_middle", "slider_light", "slider_dark");
		this.tab = tab;
	}

	@Override
	protected final void init() {
		super.init();
		new FilterTabManager(this, menu.token).init(this::addRenderableWidget, tab);
	}

	@Override
	protected void renderBg(GuiGraphics pose, float pTick, int mx, int my) {
		var sm = this.menu.sprite.get();
		var sr = sm.getRenderer(this);
		sr.start(pose);
		scroller.render(pose, sr);
		renderBgExtra(pose, sr, mx, my);
	}

	protected void renderBgExtra(GuiGraphics pose, MenuLayoutConfig.ScreenRenderer sr, int mx, int my) {
	}

	@Override
	protected void renderLabels(GuiGraphics g, int pMouseX, int pMouseY) {
		g.drawString(font, getTitle(), this.titleLabelX, this.titleLabelY, 4210752, false);
		g.drawString(font, this.playerInventoryTitle.copy().withStyle(ChatFormatting.GRAY), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}

	@Override
	public void scrollTo(int i) {
		if (i < menu.getScroll()) {
			click((menu.getScroll() - i) * 100);
		} else if (i > menu.getScroll()) {
			click(1 + (i - menu.getScroll()) * 100);
		}
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
	public boolean mouseClicked(double mx, double my, int btn) {
		var r = menu.sprite.get().getComp("grid");
		int x = r.x + getGuiLeft();
		int y = r.y + getGuiTop();
		if (mx >= x && my >= y && mx < x + r.w * r.rx && my < y + r.h * r.ry) {
			Slot slot = getSlotUnderMouse();
			if (slot != null && slot.getContainerSlot() >= menu.extra) {
				click(slot.getContainerSlot() - menu.extra + 2);
				return true;
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

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (pKeyCode == GLFW.GLFW_KEY_UP) {
			if (menu.getScroll() > 0) {
				scrollTo(menu.getScroll() - 1);
				return true;
			}
		}
		if (pKeyCode == GLFW.GLFW_KEY_DOWN) {
			if (menu.getScroll() < menu.getMaxScroll()) {
				scrollTo(menu.getScroll() + 1);
				return true;
			}
		}
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}

}
