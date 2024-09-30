package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class SetSelectScreen extends AbstractSelectScreen {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "set_select");

	public SetSelectScreen() {
		super(LangData.TITLE_SELECT_SET.get(), MANAGER, "grid");
	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		g.drawString(font, LangData.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
	}

	@Override
	protected ItemStack getStack(String comp, int x, int y) {
		int ind = x + y * 9;
		if (ind >= L2Artifacts.REGISTRATE.SET_LIST.size()) return ItemStack.EMPTY;
		var arr = L2Artifacts.REGISTRATE.SET_LIST.get(ind).items[0];
		return arr[arr.length - 1].asStack();
	}

	@Override
	public boolean mouseClicked(double mx, double my, int button) {
		if (super.mouseClicked(mx, my, button)) {
			return true;
		}
		SlotResult result = findSlot(mx, my);
		if (result == null) return false;
		int ind = result.x() + result.y() * 9;
		if (ind >= L2Artifacts.REGISTRATE.SET_LIST.size()) return false;
		Minecraft.getInstance().setScreen(new SlotSelectScreen(ind));
		return true;
	}
}
