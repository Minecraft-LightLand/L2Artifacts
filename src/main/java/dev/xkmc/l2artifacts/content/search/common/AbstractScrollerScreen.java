package dev.xkmc.l2artifacts.content.search.common;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.scroller.Scroller;
import dev.xkmc.l2artifacts.content.search.scroller.ScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class AbstractScrollerScreen<T extends AbstractScrollerMenu<T>>
		extends BaseContainerScreen<T> implements ScrollerScreen, IFilterScreen {

	private final Scroller scroller;
	private final FilterTabToken<?> tab;

	public AbstractScrollerScreen(T cont, Inventory plInv, Component title, FilterTabToken<?> tab) {
		super(cont, plInv, title);
		scroller = new Scroller(this, cont.sprite,
				"slider_middle", "slider_light", "slider_dark");
		this.tab = tab;
	}

	@Override
	protected final void init() {
		super.init();
		new FilterTabManager(this, menu.token).init(this::addRenderableWidget, tab);
	}

	@Override
	protected void renderBg(PoseStack pose, float pTick, int mx, int my) {
		SpriteManager sm = this.menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(pose);
		scroller.render(pose, sr);
		renderBgExtra(pose, sr);
	}

	protected void renderBgExtra(PoseStack pose, SpriteManager.ScreenRenderer sr) {
	}

	@Override
	protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
		this.font.draw(pPoseStack, getTitle(), this.titleLabelX, this.titleLabelY, 4210752);
		this.font.draw(pPoseStack, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752);
	}

	@Override
	public Component getTitle() {
		return super.getTitle().copy().append(": " + menu.token.getFiltered().size() + "/" + menu.token.list.size());
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
