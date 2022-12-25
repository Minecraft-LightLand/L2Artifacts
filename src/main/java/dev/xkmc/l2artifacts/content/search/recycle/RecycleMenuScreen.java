package dev.xkmc.l2artifacts.content.search.recycle;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.filter.StackedRenderHandle;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RecycleMenuScreen extends AbstractScrollerScreen<RecycleMenu> {

	public RecycleMenuScreen(RecycleMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title, FilterTabManager.RECYCLE);
	}

	@Override
	protected void renderBgExtra(PoseStack pose, SpriteManager.ScreenRenderer sr) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				int ind = i * 6 + j;
				boolean sel = ((ind < 18 ? menu.sel_0.get() >> ind : menu.sel_1.get() >> (ind - 18)) & 1) != 0;
				if (sel) {
					sr.draw(pose, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
				}
			}
		}
	}

	@Override
	protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
		pPoseStack.pushPose();
		pPoseStack.translate(0, topPos, 0);
		var handle = new StackedRenderHandle(this, pPoseStack, 8, 0xFFFFFFFF);
		handle.drawText(LangData.TAB_INFO_TOTAL.get(menu.total_count.get()));
		handle.drawText(LangData.TAB_INFO_MATCHED.get(menu.current_count.get()));
		handle.drawText(LangData.TAB_INFO_EXP.get(formatNumber(menu.experience.get())));
		handle.drawText(LangData.TAB_INFO_SELECTED.get(menu.select_count.get()));
		handle.drawText(LangData.TAB_INFO_EXP_GAIN.get(formatNumber(menu.to_gain.get())));
		handle.flushText();
		pPoseStack.popPose();
		super.renderTooltip(pPoseStack, pX, pY);
	}

	private static final String[] SUFFIX = {"", "k", "M", "G", "T"};

	private static String formatNumber(int number) {
		int level = 0;
		while (true) {
			if (number < 10000 || level == SUFFIX.length - 1) {
				return number + SUFFIX[level];
			}
			number = (int) Math.round(number * 1e-3);
			level++;
		}
	}


}