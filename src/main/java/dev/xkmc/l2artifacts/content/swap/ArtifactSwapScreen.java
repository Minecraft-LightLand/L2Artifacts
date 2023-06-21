package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapScreen extends BaseContainerScreen<ArtifactSwapMenu> {

	public ArtifactSwapScreen(ArtifactSwapMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
		var sm = this.menu.sprite.get();
		var sr = sm.getRenderer(this);
		sr.start(g);
		for (int i = 0; i < 45; i++) {
			drawDisable(sr, g, i);
		}
	}

	public void drawDisable(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, int i) {
		boolean lock = menu.disable.get(i);
		ArtifactSlot slot = menu.data.contents[i].slot;
		ItemStack stack = menu.container.getItem(i);
		if (lock) {
			sr.draw(g, "grid", "altas_disabled", (i % 9) * 18, (i / 9) * 18);
		} else if (stack.isEmpty()) {
			sr.draw(g, "grid", "altas_" + slot.getRegistryName().getPath(), (i % 9) * 18, (i / 9) * 18);
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		var r = menu.sprite.get().getComp("grid");
		int x = r.x + getGuiLeft();
		int y = r.y + getGuiTop();
		if (mx >= x && my >= y && mx < x + r.w * r.rx && my < y + r.h * r.ry) {
			Slot slot = getSlotUnderMouse();
			if (slot != null && slot.getItem().isEmpty() && menu.getCarried().isEmpty()) {
				click(slot.getContainerSlot());
				return true;
			}
		}
		return super.mouseClicked(mx, my, btn);
	}

}
