package dev.xkmc.l2artifacts.content.client.search.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.client.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class BaseFilterScreen extends Screen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filter");


	public final ArtifactChestToken token;

	private int imageWidth, imageHeight, leftPos, topPos;

	@Nullable
	private FilterHover hover;

	protected BaseFilterScreen(Component title, ArtifactChestToken token) {
		super(title);
		this.token = token;
	}

	@Override
	protected void init() {
		this.imageWidth = MANAGER.getWidth();
		this.imageHeight = MANAGER.getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;
	}

	public void render(PoseStack pose, int mx, int my, float pTick) {
		renderBg(pose, pTick, mx, my);
		RenderSystem.disableDepthTest();
		super.render(pose, mx, my, pTick);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(leftPos, topPos, 0.0D);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		StackedRenderHandle handle = new StackedRenderHandle(this, pose);
		hover = null;
		for (int i = 0; i < token.filters.size(); i++) {
			var filter = token.filters.get(i);
			handle.drawText(filter.getDescription());
			boolean[] available = filter.getAvailability();
			for (int j = 0; j < filter.allEntries.size(); j++) {
				boolean selected = filter.getSelected(j);
				var item = filter.allEntries.get(j);
				if (handle.addCell(selected, !available[j], (x, y) -> {
					if (item instanceof IArtifactFeature.Sprite icon) {
						icon.getIcon();
					} else if (item instanceof IArtifactFeature.ItemIcon icon) {
						renderSlotItem(x, y, icon.getItemIcon().getDefaultInstance());
					}
					boolean hover = isHovering(x, y, 16, 16, mx, my);
					if (hover) {
						AbstractContainerScreen.renderSlotHighlight(pose, x, y, getBlitOffset(), -2130706433);
					}
					return hover;
				})) {
					hover = new FilterHover(i, j);
				}
			}
		}
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
	}

	private void renderBg(PoseStack stack, float pt, int mx, int my) {
		SpriteManager.ScreenRenderer sr = MANAGER.new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(stack);
	}

	private boolean isHovering(int x, int y, int w, int h, double mx, double my) {
		int i = this.leftPos;
		int j = this.topPos;
		mx -= i;
		my -= j;
		return mx >= (x - 1) && mx < (x + w + 1) && my >= (y - 1) && my < (y + h + 1);
	}

	private void renderSlotItem(int x, int y, ItemStack stack) {
		String s = null;
		this.setBlitOffset(100);
		this.itemRenderer.blitOffset = 100.0F;
		RenderSystem.enableDepthTest();
		assert this.minecraft != null;
		assert this.minecraft.player != null;
		this.itemRenderer.renderAndDecorateItem(this.minecraft.player, stack, x, y, x + y * this.imageWidth);
		this.itemRenderer.renderGuiItemDecorations(this.font, stack, x, y, s);
		this.itemRenderer.blitOffset = 0.0F;
		this.setBlitOffset(0);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		if (hover != null) {
			token.filters.get(hover.i).toggle(hover.j);
		}
		return false;
	}

	private record FilterHover(int i, int j) {

	}

}
