package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.events.ArtifactClientHandler;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SelectArtifactItem extends Item {

	public SelectArtifactItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		player.awardStat(Stats.ITEM_USED.get(this));
		if (level.isClientSide) {
			ArtifactClientHandler.openSelectionScreen();
		}
		return InteractionResultHolder.success(itemstack);
	}

}
