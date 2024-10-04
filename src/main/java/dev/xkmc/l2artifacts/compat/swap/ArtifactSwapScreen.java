package dev.xkmc.l2artifacts.compat.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2backpack.content.quickswap.set.GenericSetBagSlot;
import dev.xkmc.l2backpack.content.quickswap.set.GenericSetSwapScreen;
import dev.xkmc.l2core.base.menu.base.MenuLayoutConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapScreen extends GenericSetSwapScreen<ArtifactSwapMenu> {

	public ArtifactSwapScreen(ArtifactSwapMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderSlot(GuiGraphics g, Slot slot) {
		if (slot instanceof GenericSetBagSlot s) {
			if (!s.isDisabled()) {
				int i = s.getContainerSlot();
				ArtifactSlot a = ArtifactSwapType.getSlot(i / 9);
				 if (s.getItem().isEmpty()) {
					 getRenderer().draw(g, "grid", "altas_" + a.getRegistryName().getPath(),
							 (i % 9) * 18, (i / 9) * 18);
				}
			}
		}
		super.renderSlot(g, slot);
	}

	@Override
	protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
		p_281635_.drawString(this.font, this.title.copy().withStyle(ChatFormatting.GRAY), this.titleLabelX, this.titleLabelY, 4210752, false);
		p_281635_.drawString(this.font, this.playerInventoryTitle.copy().withStyle(ChatFormatting.GRAY), this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}

}
