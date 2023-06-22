package dev.xkmc.l2artifacts.content.search.common;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.xkmc.l2artifacts.content.client.tooltip.ItemTooltip;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabToken;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class StackedScreen extends Screen implements IFilterScreen {

	public static void renderHighlight(GuiGraphics g, int x, int y, int w, int h, int c) {
		g.fillGradient(RenderType.guiOverlay(), x, y, x + w, y + h, c, c, 0);
	}

	public final ArtifactChestToken token;

	private final SpriteManager manager;
	private final FilterTabToken<?> tab;

	private int imageWidth, imageHeight, leftPos, topPos;

	protected boolean pressed = false;

	@Nullable
	private FilterHover hover;

	protected StackedScreen(Component title, SpriteManager manager, FilterTabToken<?> tab, ArtifactChestToken token) {
		super(title);
		this.token = token;
		this.tab = tab;
		this.manager = manager;
	}

	@Override
	protected void init() {
		this.imageWidth = manager.get().getWidth();
		this.imageHeight = manager.get().getHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;
		new FilterTabManager(this, token).init(this::addRenderableWidget, tab);
	}

	protected void renderInit() {
	}

	protected void renderPost(GuiGraphics pose) {
	}

	protected abstract void renderText(StackedRenderHandle handle, int i, int mx, int my);

	protected abstract boolean isAvailable(int i, int j);

	protected void renderItem(GuiGraphics g, FilterHover hover) {
		if (hover.item instanceof IArtifactFeature.Sprite icon) {
			g.blit(icon.getIcon(), hover.x, hover.y, 0, 0, 16, 16, 16, 16);
		} else if (hover.item instanceof IArtifactFeature.ItemIcon icon) {
			renderSlotItem(g, hover.x, hover.y, icon.getItemIcon().getDefaultInstance());
		}
	}

	public final void render(GuiGraphics g, int mx, int my, float pTick) {
		renderBg(g, pTick, mx, my);
		g.pose().pushPose();
		g.pose().translate(leftPos, topPos, 0);
		StackedRenderHandle handle = new StackedRenderHandle(this, g, manager.get());
		hover = null;
		renderInit();
		List<FilterHover> list = new ArrayList<>();
		for (int i = 0; i < token.filters.size(); i++) {
			var filter = token.filters.get(i);
			renderText(handle, i, mx, my);
			for (int j = 0; j < filter.allEntries.size(); j++) {
				boolean selected = filter.getSelected(j);
				var item = filter.allEntries.get(j);
				var entry = handle.addCell(selected, !isAvailable(i, j));
				var obj = new FilterHover(item, i, j, entry.x(), entry.y());
				if (isHovering(entry.x(), entry.y(), 16, 16, mx, my)) {
					hover = obj;
				}
				list.add(obj);
			}
		}
		handle.flushText();
		list.forEach(e -> renderItem(g, e));
		if (hover != null) {
			renderHighlight(g, hover.x, hover.y, 16, 16, -2130706433);
			List<Component> texts = new ArrayList<>();
			texts.add(hover.item().getDesc());
			Optional<TooltipComponent> comp = Optional.ofNullable(hover.item().getTooltipItems())
					.map(ItemTooltip::new);
			g.renderTooltip(font, texts, comp, mx - leftPos, my - topPos);
		}
		renderPost(g);
		g.pose().popPose();
		super.render(g, mx, my, pTick);
	}

	private void renderBg(GuiGraphics g, float pt, int mx, int my) {
		var sr = manager.get().new ScreenRenderer(this, leftPos, topPos, imageWidth, imageHeight);
		sr.start(g);
	}

	protected boolean isHovering(int x, int y, int w, int h, double mx, double my) {
		int i = this.leftPos;
		int j = this.topPos;
		mx -= i;
		my -= j;
		return mx >= (x - 1) && mx < (x + w + 1) && my >= (y - 1) && my < (y + h + 1);
	}

	private void renderSlotItem(GuiGraphics g, int x, int y, ItemStack stack) {
		RenderSystem.enableDepthTest();
		assert this.minecraft != null;
		assert this.minecraft.player != null;
		g.renderItem(stack, x, y, x + y * this.imageWidth);
		g.renderItemDecorations(this.font, stack, x, y, null);
	}

	protected abstract void clickHover(int i, int j);

	public void onSwitch() {
		var filter = TagCodec.toTag(new CompoundTag(), token);
		assert filter != null;
		ArtifactChestItem.setFilter(Proxy.getClientPlayer().getInventory().getItem(token.invSlot), filter);
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		pressed = true;
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		if (hover != null) {
			clickHover(hover.i, hover.j);
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		pressed = false;
		return super.mouseReleased(pMouseX, pMouseY, pButton);
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

	protected record FilterHover(IArtifactFeature item, int i, int j, int x, int y) {

	}

}
