package dev.xkmc.l2artifacts.content.search.dissolve;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class DissolveMenuScreen extends AbstractScrollerScreen<DissolveMenu> {

	public DissolveMenuScreen(DissolveMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, LangData.TAB_DISSOLVE.get(), FilterTabManager.DISSOLVE);
	}

	@Override
	protected void renderBgExtra(PoseStack pose, SpriteManager.ScreenRenderer sr, int mx, int my) {
		ItemStack stack = menu.container.getItem(0);
		int rank = stack.isEmpty() ? 0 : ((StatContainerItem) stack.getItem()).rank;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (menu.select_index.get() == i * 6 + j) {
					sr.draw(pose, "grid", "toggle_slot_1", j * 18 - 1, i * 18 - 1);
				} else {
					ItemStack art = menu.container.getItem(i * 6 + j + 2);
					int r = art.isEmpty() ? 0 : ((BaseArtifact) art.getItem()).rank;
					if (rank > 0 && r > 0 && rank != r) {
						sr.draw(pose, "grid", "toggle_slot_2", j * 18 - 1, i * 18 - 1);
					}
				}

			}
		}
	}

	@Override
	public Component getTitle() {
		return super.getTitle().copy().append(": " + menu.current_count.get() + "/" + menu.total_count.get());
	}

}
