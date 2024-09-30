package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.network.ChooseArtifactToServer;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class RankSelectScreen extends AbstractSelectScreen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "rank_select");

	private final int set, slot;

	protected RankSelectScreen(int set, int slot) {
		super(ArtifactLang.TITLE_SELECT_SLOT.get(), MANAGER, "set", "slot", "rank");
		this.set = set;
		this.slot = slot;
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		g.drawString(font, ArtifactLang.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
		g.drawString(font, ArtifactLang.TITLE_SELECT_SLOT.get(), 8, 6 + 13 + 18, 4210752, false);
		g.drawString(font, ArtifactLang.TITLE_SELECT_RANK.get(), 8, 6 + (13 + 18) * 2, 4210752, false);
	}

	@Override
	protected ItemStack getStack(String comp, int x, int y) {
		var setEntry = L2Artifacts.REGISTRATE.SET_LIST.get(set);
		if (comp.equals("set")) return setEntry.items[0][setEntry.items[0].length - 1].asStack();
		if (comp.equals("slot")) return setEntry.items[slot][setEntry.items[slot].length - 1].asStack();
		int n = setEntry.items[slot].length;
		return x < n ? setEntry.items[slot][x].asStack() : ItemStack.EMPTY;
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		SlotResult result = findSlot(mx, my);
		if (result == null) return false;
		if (result.name().equals("set")) {
			Minecraft.getInstance().setScreen(new SetSelectScreen());
			return true;
		}
		if (result.name().equals("slot")) {
			Minecraft.getInstance().setScreen(new SlotSelectScreen(set));
			return true;
		}
		int ind = result.x();
		var setEntry = L2Artifacts.REGISTRATE.SET_LIST.get(set);
		int n = setEntry.items[slot].length;
		if (ind >= n) return false;
		Minecraft.getInstance().setScreen(null);
		NetworkManager.HANDLER.toServer(new ChooseArtifactToServer(set, slot, ind));
		return true;
	}

}
