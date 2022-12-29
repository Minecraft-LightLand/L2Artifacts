package dev.xkmc.l2artifacts.content.search.tabs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class FilterTabBase<T extends FilterTabBase<T>> extends Button {

	private final static ResourceLocation TEXTURE = new ResourceLocation(L2Artifacts.MODID, "textures/gui/tabs.png");

	public final ItemStack stack;
	public final FilterTabToken<T> token;
	public final FilterTabManager manager;

	@SuppressWarnings("unchecked")
	public FilterTabBase(FilterTabToken<T> token, FilterTabManager manager, ItemStack stack, Component title) {
		super(0, 0, 32, 28, title, b -> ((T) b).onTabClicked());
		this.stack = stack;
		this.token = token;
		this.manager = manager;
	}

	public abstract void onTabClicked();

	public void onTooltip(PoseStack stack, int x, int y) {
		manager.getScreen().renderTooltip(stack, getMessage(), x, y);
	}

	public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, TEXTURE);
			token.type.draw(stack, manager.getScreen(), x, y, manager.selected == token, token.index);
			RenderSystem.defaultBlendFunc();
			token.type.drawIcon(x, y, token.index, Minecraft.getInstance().getItemRenderer(), this.stack);
		}
		if (this.token.index == FilterTabManager.LIST.size() - 1) { // draw on last
			manager.onToolTipRender(stack, mouseX, mouseY);
		}
	}

}
