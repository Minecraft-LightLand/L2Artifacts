package dev.xkmc.l2artifacts.content.client.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public record ClientItemTooltip(ItemTooltip items) implements ClientTooltipComponent {

	@Override
	public int getHeight() {
		return 18;
	}

	@Override
	public int getWidth(Font font) {
		return 18 * items.list().size();
	}

	@Override
	public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix4f, MultiBufferSource.BufferSource pBufferSource) {

	}

	@Override
	public void renderImage(Font pFont, int pMouseX, int pMouseY, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
		for (int i = 0; i < items.list().size(); ++i) {
			int x = pMouseX + i * 18 + 1;
			int y = pMouseY + 1;
			ItemStack itemstack = this.items.list().get(i);
			pItemRenderer.renderAndDecorateItem(itemstack, x, y, i);
			pItemRenderer.renderGuiItemDecorations(pFont, itemstack, x, y);
		}
	}

}
