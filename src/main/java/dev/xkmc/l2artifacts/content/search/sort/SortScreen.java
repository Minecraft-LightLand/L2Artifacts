package dev.xkmc.l2artifacts.content.search.sort;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.base.menu.stacked.CellEntry;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class SortScreen extends StackedScreen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "sort");

	@Nullable
	private ButtonHover btnHover;

	protected SortScreen(ArtifactChestToken token) {
		super(LangData.TAB_SORT.get(), MANAGER, FilterTabManager.SORT, token);
	}

	private ButtonHover prevBtnHover;

	protected void renderInit() {
		prevBtnHover = btnHover;
		btnHover = null;
	}

	protected void renderPost(PoseStack pose) {
		if (btnHover != null) {
			var cell = btnHover.cell();
			renderHighlight(pose, cell.x(), cell.y(), cell.w(), cell.h(), -2130706433);
		}
	}

	protected void renderText(StackedRenderHandle handle, int i, int mx, int my) {
		boolean p = pressed && prevBtnHover != null && prevBtnHover.i() == i;
		var btns = handle.drawTextWithButtons(token.filters.get(i).getDescription());
		var ca = btns.addButton(p ? "sort_1" : "sort_1p");
		btns.drawText(ca, Component.literal("" + token.filters.get(i).priority()));
		if (isHovering(ca.x(), ca.y(), ca.w(), ca.h(), mx, my)) {
			btnHover = new ButtonHover(i, ca);
		}
	}

	@Override
	protected void renderItem(PoseStack pose, FilterHover hover) {
		super.renderItem(pose, hover);
		String s = token.filters.get(hover.i()).getPriority(hover.j()) + "";
		pose.pushPose();
		pose.translate(0.0D, 0.0D, 300.0F);
		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		int tx = hover.x() + 19 - 2 - font.width(s);
		int ty = hover.y() + 6 + 3;
		font.drawInBatch(s, tx, ty, 16777215, true, pose.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
		buffer.endBatch();
		pose.popPose();
	}

	@Override
	protected boolean isAvailable(int i, int j) {
		var filter = token.filters.get(i);
		return filter.getSelected(j) || filter.getAvailability(j);
	}

	@Override
	protected void clickHover(int i, int j) {
		if (!isAvailable(i, j)) return;
		token.filters.get(i).prioritize(j);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		pressed = false;
		if (btnHover != null) {
			token.prioritize(btnHover.i());
			return true;
		}
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	private record ButtonHover(int i, CellEntry cell) {

	}

}
