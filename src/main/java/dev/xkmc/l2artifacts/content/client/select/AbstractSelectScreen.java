package dev.xkmc.l2artifacts.content.client.select;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
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
		this.imageHeight = manager.getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;
	}

	public void render(PoseStack pose, int mx, int my, float pTick) {
		this.renderBg(pose, pTick, mx, my);
		RenderSystem.disableDepthTest();
		super.render(pose, mx, my, pTick);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(leftPos, topPos, 0.0D);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		hovered = null;
		for (String c : slots) {
			renderSlotComp(pose, c, mx, my);
		}
		this.renderLabels(pose, mx, my);
		if (hovered != null && !hovered.isEmpty()) {
			pose.pushPose();
			pose.translate(-leftPos, -topPos, 0);
			this.renderTooltip(pose, hovered, mx, my);
			pose.popPose();
		}
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
	}

	protected abstract void renderLabels(PoseStack pose, int mx, int my);

	protected abstract ItemStack getStack(String comp, int x, int y);

	private void renderSlotComp(PoseStack pose, String name, int mx, int my) {
		var comp = manager.getComp(name);
		for (int i = 0; i < comp.rx; i++) {
			for (int j = 0; j < comp.ry; j++) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
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

	private void renderSlot(PoseStack pose, int x, int y, ItemStack stack) {
		String s = null;
		RenderSystem.enableDepthTest();
		assert this.minecraft != null;
		assert this.minecraft.player != null;
		this.itemRenderer.renderAndDecorateItem(pose, this.minecraft.player, stack, x, y, x + y * this.imageWidth);
		this.itemRenderer.renderGuiItemDecorations(pose, this.font, stack, x, y, s);
		//TODO fix
	}

	private void renderBg(PoseStack stack, float pt, int mx, int my) {
		manager.getWidth();// call check();
		SpriteManager.ScreenRenderer sr = manager.new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(stack);
	}

	private boolean isHovering(String slot, int i, int j, double mx, double my) {
		var comp = manager.getComp(slot);
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
			var comp = manager.getComp(c);
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
