package dev.xkmc.l2artifacts.content.search.sort;

import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2core.base.menu.stacked.CellEntry;
import dev.xkmc.l2core.base.menu.stacked.StackedRenderHandle;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nullable;

public class FilterScreen extends StackedScreen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filter");

	@Nullable
	private ButtonHover btnHover;

	protected FilterScreen(ArtifactTabData token) {
		super(ArtifactLang.TAB_FILTER.get(), MANAGER, ArtifactTabRegistry.FILTER.get(), token);
	}

	private ButtonHover prevBtnHover;

	protected void renderInit() {
		prevBtnHover = btnHover;
		btnHover = null;
	}

	protected void renderPost(GuiGraphics g) {
		if (btnHover != null) {
			var cell = btnHover.cell();
			renderHighlight(g, cell.x(), cell.y(), cell.w(), cell.h(), -2130706433);
		}
	}

	protected void renderText(StackedRenderHandle handle, int i, int mx, int my) {
		boolean p = pressed && prevBtnHover != null && prevBtnHover.i() == i;
		boolean pa = p && prevBtnHover.a();
		boolean pb = p && !prevBtnHover.a();
		var btns = handle.drawTextWithButtons(token.getDescription(i), false);
		var ca = btns.addButton(pa ? "button_1" : "button_1p");
		var cb = btns.addButton(pb ? "button_2" : "button_2p");
		if (isHovering(ca.x(), ca.y(), ca.w(), ca.h(), mx, my)) {
			btnHover = new ButtonHover(i, true, ca);
		}
		if (isHovering(cb.x(), cb.y(), cb.w(), cb.h(), mx, my)) {
			btnHover = new ButtonHover(i, false, cb);
		}
	}

	@Override
	protected boolean isAvailable(int i, int j) {
		return token.isAvailable(i, j, false);
	}

	@Override
	protected void clickHover(int i, int j) {
		token.toggleSel(i, j);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		pressed = false;
		if (btnHover != null) {
			token.setAllSel(btnHover.i(), btnHover.a());
			return true;
		}
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	private record ButtonHover(int i, boolean a, CellEntry cell) {

	}

}
