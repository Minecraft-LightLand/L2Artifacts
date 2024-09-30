package dev.xkmc.l2artifacts.content.search.upgrade;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.search.filter.FilterScreen;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenuScreen;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2core.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2core.base.menu.stacked.StackedRenderHandle;
import dev.xkmc.l2tabs.tabs.core.ITabScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UpgradeMenuScreen extends BaseContainerScreen<UpgradeMenu> implements ITabScreen {

	private static int lerpColor(float perc, int fg, int bg) {
		int c0 = Math.round(Mth.lerp(perc, fg & 0xFF, bg & 0xFF));
		fg >>= 8;
		bg >>= 8;
		int c1 = Math.round(Mth.lerp(perc, fg & 0xFF, bg & 0xFF));
		fg >>= 8;
		bg >>= 8;
		int c2 = Math.round(Mth.lerp(perc, fg & 0xFF, bg & 0xFF));
		return c2 << 16 | c1 << 8 | c0;
	}

	private static final int MAX_TIME = 60;
	private static final ChatFormatting MAIN = ChatFormatting.DARK_GREEN;
	private static final ChatFormatting SUB = ChatFormatting.BLUE;

	private boolean pressed = false;

	private float time = 0;
	@Nullable
	private ArtifactStats old = null, current = null;
	private ItemStack oldStack = ItemStack.EMPTY;
	private boolean keep = false;

	public UpgradeMenuScreen(UpgradeMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, ArtifactLang.TAB_UPGRADE.get().withStyle(ChatFormatting.GRAY));
	}

	@Override
	protected final void init() {
		super.init();
		new TabManager<>(this, menu.token).init(this::addRenderableWidget, ArtifactTabRegistry.UPGRADE.get());
	}

	@Override
	protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
		var sr = getRenderer();
		sr.start(g);
		var rect = menu.getLayout().getComp("upgrade");
		if (isHovering(rect.x, rect.y, rect.w, rect.h, mx, my)) {
			if (pressed) {
				sr.draw(g, "upgrade", "upgrade_on");
			}
			FilterScreen.renderHighlight(g, leftPos + rect.x, topPos + rect.y, rect.w, rect.h, -2130706433);
		}
		if (time > 0) {
			time -= Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
			if (time <= 0) {
				time = 0;
				old = null;
			}
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		pressed = true;
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	public boolean mouseReleased(double mx, double my, int btn) {
		pressed = false;
		var rect = menu.getLayout().getComp("upgrade");
		if (isHovering(rect.x, rect.y, rect.w, rect.h, mx, my)) {
			old = current;
			time = MAX_TIME;
			keep = click(0);
		}
		return super.mouseReleased(mx, my, btn);
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		g.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		g.drawString(this.font, this.playerInventoryTitle.copy().withStyle(ChatFormatting.GRAY), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
		g.pose().pushPose();
		g.pose().translate(0, 45, 0);
		StackedRenderHandle handle = new StackedRenderHandle(this, g, getRenderer(), 0);
		int exp = menu.experience.get();
		int cost = menu.exp_cost.get();
		if (cost > 0) {
			String str = RecycleMenuScreen.formatNumber(cost) + "/" + RecycleMenuScreen.formatNumber(exp);
			handle.drawText(ArtifactLang.TAB_INFO_EXP_COST.get(Component.literal(str)
							.withStyle(cost <= exp ? ChatFormatting.DARK_GREEN : ChatFormatting.RED))
					.withStyle(ChatFormatting.GRAY), false);
			ItemStack stack = menu.container.getItem(0);
			if (stack != oldStack) {
				oldStack = stack;
				if (keep) keep = false;
				else old = null;
			}
			var opt = BaseArtifact.getStats(stack);
			if (opt.isPresent()) {
				ArtifactStats stat = opt.get();
				List<Component[]> table = new ArrayList<>();
				table.add(addEntry(true, stat.main_stat(),
						old == null ? null : old.main_stat()));
				boolean display = old != null && old.sub_stats().size() == stat.sub_stats().size();
				for (int i = 0; i < stat.sub_stats().size(); i++) {
					table.add(addEntry(false, stat.sub_stats().get(i),
							!display ? null : old.sub_stats().get(i)));
				}
				handle.drawTable(table.toArray(Component[][]::new), imageWidth, false);
				current = stat;
			}
		} else {
			handle.drawText(ArtifactLang.TAB_INFO_EXP.get(exp).withStyle(ChatFormatting.GRAY), false);
		}
		handle.flushText();
		g.pose().popPose();
	}

	private Component[] addEntry(boolean main, StatEntry entry, @Nullable StatEntry old) {
		Component[] ans = new Component[3];
		ans[0] = entry.getType().value().getDesc().withStyle(main ? MAIN : SUB);
		ans[1] = entry.getType().value().getValueText(entry.getValue()).withStyle(main ? MAIN : SUB);
		if (old != null) {
			double diff = entry.getValue() - old.getValue();
			if (diff > 1e-3) {
				Integer fg = ChatFormatting.DARK_PURPLE.getColor();
				assert fg != null;
				float perc = 1 - time / MAX_TIME;
				int c = lerpColor(perc, fg, 0x33322E);
				ans[2] = entry.getType().value().getValueText(diff).withStyle(Style.EMPTY.withColor(c));
			} else {
				ans[2] = Component.empty();
			}
		} else {
			ans[2] = Component.empty();
		}
		entry.getTooltip();
		return ans;
	}

	@Override
	public int screenWidth() {
		return width;
	}

	@Override
	public int screenHeight() {
		return height;
	}

}
