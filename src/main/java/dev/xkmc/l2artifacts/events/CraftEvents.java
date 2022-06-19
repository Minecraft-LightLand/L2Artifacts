package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class CraftEvents {

	@SubscribeEvent
	public static void onAnvilCraft(AnvilUpdateEvent event) {
		ItemStack stack = event.getLeft();
		ItemStack fodder = event.getRight();
		int val = 0;
		if (fodder.getItem() instanceof BaseArtifact base) {
			val = ArtifactUpgradeManager.getExpForConversion(base.rank, BaseArtifact.getStats(fodder).orElse(null));
		} else if (fodder.getItem() instanceof ExpItem exp) {
			val = ArtifactUpgradeManager.getExpForConversion(exp.rank, null);
		}
		if (val > 0 && stack.getItem() instanceof BaseArtifact) {
			Optional<ArtifactStats> opt = BaseArtifact.getStats(stack);
			if (opt.isPresent()) {
				ItemStack new_stack = stack.copy();
				BaseArtifact.upgrade(new_stack, val, event.getPlayer().getRandom());
				event.setOutput(new_stack);
				event.setMaterialCost(1);
				event.setCost((int) Math.ceil(Math.log(val)));
			}
		}
	}

}
