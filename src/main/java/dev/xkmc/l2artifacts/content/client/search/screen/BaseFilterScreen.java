package dev.xkmc.l2artifacts.content.client.search.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BaseFilterScreen extends Screen {

	public final SpriteManager manager;
	public final ArtifactChestToken token;

	private int imageWidth, imageHeight, leftPos, topPos;

	protected BaseFilterScreen(Component title, SpriteManager manager, ArtifactChestToken token) {
		super(title);
		this.manager = manager;
		this.token = token;
	}

	@Override
	protected void init() {
		this.imageWidth = 176;
		this.imageHeight = manager.getHeight();
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
		for (int i = 0; i < token.filters.size(); i++) {
			var filter = token.filters.get(i);
			boolean[] available = filter.getAvailability();
			for (int j = 0; j < filter.allEntries.size(); j++) {
				boolean selected = filter.getSelected(j);
				boolean hover = false;

			}
		}
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
	}

	private void renderBg(PoseStack stack, float pt, int mx, int my) {
		manager.getWidth();// call check();
		SpriteManager.ScreenRenderer sr = manager.new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(stack);
	}

}
