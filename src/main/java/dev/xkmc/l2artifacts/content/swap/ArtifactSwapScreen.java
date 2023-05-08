package dev.xkmc.l2artifacts.content.swap;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2library.base.menu.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapScreen extends BaseContainerScreen<ArtifactSwapMenu> {

	public ArtifactSwapScreen(ArtifactSwapMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(PoseStack pose, float pTick, int mx, int my) {
		SpriteManager sm = this.menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(pose);
		for (int i = 0; i < 45; i++) {
			drawDisable(sr, pose, i);
		}
	}

	public void drawDisable(SpriteManager.ScreenRenderer sr, PoseStack pose, int i) {
		boolean lock = (i < 30 ? (menu.lower.get() & (1 << i)) : (menu.upper.get() & (1 << i - 30))) != 0;
		ArtifactSlot slot = menu.data.contents[i].slot;
		ItemStack stack = menu.container.getItem(i);
		if (lock) {
			sr.draw(pose, "grid", "altas_disabled", (i % 9) * 18, (i / 9) * 18);
		} else if (stack.isEmpty()) {
			sr.draw(pose, "grid", "altas_" + slot.getRegistryName().getPath(), (i % 9) * 18, (i / 9) * 18);
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		SpriteManager.Rect r = menu.sprite.getComp("grid");
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
