package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2library.base.menu.base.SpriteManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public abstract class AbstractSelectScreen extends Screen {

	public final SpriteManager manager;
	public final String[] slots;

	private int imageWidth, imageHeight, leftPos, topPos;

	private ItemStack hovered = null;

	protected AbstractSelectScreen(Component title, SpriteManager manager, String... slots) {
		super(title);
		this.manager = manager;
		this.slots = slots;

	}

	@Override
	protected void init() {
		this.imageWidth = 176;
		this.imageHeight = manager.get().getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;
	}

	public void render(GuiGraphics g, int mx, int my, float pTick) {
		this.renderBg(g, pTick, mx, my);
		super.render(g, mx, my, pTick);
		g.pose().pushPose();
		g.pose().translate(leftPos, topPos, 0.0D);
		hovered = null;
		for (String c : slots) {
			renderSlotComp(g, c, mx, my);
		}
		this.renderLabels(g, mx, my);
		if (hovered != null && !hovered.isEmpty()) {
			g.pose().pushPose();
			g.pose().translate(-leftPos, -topPos, 0);
			g.renderTooltip(font, hovered, mx, my);
			g.pose().popPose();
		}
		g.pose().popPose();
	}

	protected abstract void renderLabels(GuiGraphics pose, int mx, int my);

	protected abstract ItemStack getStack(String comp, int x, int y);

	private void renderSlotComp(GuiGraphics pose, String name, int mx, int my) {
		var comp = manager.get().getComp(name);
		for (int i = 0; i < comp.rx; i++) {
			for (int j = 0; j < comp.ry; j++) {
				int sx = comp.x + comp.w * i;
				int sy = comp.y + comp.h * j;
				ItemStack stack = getStack(name, i, j);
				this.renderSlot(pose, sx, sy, stack);
				if (this.isHovering(name, i, j, mx, my)) {
					AbstractContainerScreen.renderSlotHighlight(pose, sx, sy, -2130706433);
					hovered = stack;
				}
			}
		}
	}

	private void renderSlot(GuiGraphics g, int x, int y, ItemStack stack) {
		String s = null;
		assert this.minecraft != null;
		assert this.minecraft.player != null;
		g.renderItem(stack, x, y, x + y * this.imageWidth);
		g.renderItemDecorations(this.font, stack, x, y, s);
	}

	private void renderBg(GuiGraphics stack, float pt, int mx, int my) {
		var sr = manager.get().new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(stack);
	}

	private boolean isHovering(String slot, int i, int j, double mx, double my) {
		var comp = manager.get().getComp(slot);
		return this.isHovering(comp.x + comp.w * i, comp.y + comp.h * j, 16, 16, mx, my);
	}

	private boolean isHovering(int x, int y, int w, int h, double mx, double my) {
		int i = this.leftPos;
		int j = this.topPos;
		mx -= i;
		my -= j;
		return mx >= (x - 1) && mx < (x + w + 1) && my >= (y - 1) && my < (y + h + 1);
	}

	@Nullable
	protected SlotResult findSlot(double mx, double my) {
		for (String c : slots) {
			var comp = manager.get().getComp(c);
			for (int i = 0; i < comp.rx; i++) {
				for (int j = 0; j < comp.ry; j++) {
					if (this.isHovering(c, i, j, mx, my)) {
						return new SlotResult(c, i, j);
					}
				}
			}
		}
		return null;
	}

	public record SlotResult(String name, int x, int y) {
	}

}
