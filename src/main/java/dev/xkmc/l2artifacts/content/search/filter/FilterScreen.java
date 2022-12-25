package dev.xkmc.l2artifacts.content.search.filter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FilterScreen extends Screen implements IFilterScreen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filter");


	public final ArtifactChestToken token;

	private int imageWidth, imageHeight, leftPos, topPos;

	@Nullable
	private FilterHover hover;

	protected FilterScreen(ArtifactChestToken token) {
		super(LangData.TAB_FILTER.get());
		this.token = token;
	}

	@Override
	protected void init() {
		this.imageWidth = MANAGER.getWidth();
		this.imageHeight = MANAGER.getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;
		new FilterTabManager(this, token).init(this::addRenderableWidget, FilterTabManager.FILTER);
	}

	public void render(PoseStack pose, int mx, int my, float pTick) {
		renderBg(pose, pTick, mx, my);
		RenderSystem.disableDepthTest();
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.translate(leftPos, topPos, 0.0D);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		StackedRenderHandle handle = new StackedRenderHandle(this, pose);
		hover = null;
		List<FilterHover> list = new ArrayList<>();
		for (int i = 0; i < token.filters.size(); i++) {
			var filter = token.filters.get(i);
			handle.drawText(filter.getDescription());
			boolean[] available = filter.getAvailability();
			for (int j = 0; j < filter.allEntries.size(); j++) {
				boolean selected = filter.getSelected(j);
				var item = filter.allEntries.get(j);
				var entry = handle.addCell(selected, !available[j]);
				var obj = new FilterHover(item, i, j, entry.x(), entry.y());
				if (isHovering(entry.x(), entry.y(), 16, 16, mx, my)) {
					hover = obj;
				}
				list.add(obj);
			}
		}
		handle.flushText();
		list.forEach(e -> e.draw(this, pose));
		if (hover != null) {
			AbstractContainerScreen.renderSlotHighlight(pose, hover.x, hover.y, getBlitOffset(), -2130706433);
			List<Component> texts = new ArrayList<>();
			texts.add(hover.item().getDesc());
			Optional<TooltipComponent> comp = Optional.ofNullable(hover.item().getTooltipItems())
					.map(l -> new BundleTooltip(l, 0));
			renderTooltip(pose, texts, comp, mx - leftPos, my - topPos);
		}
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.enableDepthTest();
		super.render(pose, mx, my, pTick);
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

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public int getGuiLeft() {
		return leftPos;
	}

	@Override
	public int getGuiTop() {
		return topPos;
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
	public int getXSize() {
		return imageWidth;
	}

	@Override
	public int getYSize() {
		return imageHeight;
	}

	private record FilterHover(IArtifactFeature item, int i, int j, int x, int y) {

		private void draw(FilterScreen screen, PoseStack pose) {
			if (item instanceof IArtifactFeature.Sprite icon) {
				RenderSystem.setShaderTexture(0, icon.getIcon());
				blit(pose, this.x, this.y, 0, 0, 16, 16, 16, 16);
			} else if (item instanceof IArtifactFeature.ItemIcon icon) {
				screen.renderSlotItem(x, y, icon.getItemIcon().getDefaultInstance());
			}
		}

	}

}
