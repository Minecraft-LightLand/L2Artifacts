package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RandomArtifactItem extends Item {

	public final int rank;

	public RandomArtifactItem(Properties props, int rank) {
		super(props);
		this.rank = rank;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}
		if (!level.isClientSide) {
			player.getInventory().placeItemBackInInventory(getRandomArtifact(rank, player.getRandom()));
		}
		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

	private static ItemStack getRandomArtifact(int rank, RandomSource random) {
		var sets = L2Artifacts.REGISTRATE.SET_LIST.stream().filter(e -> e.hasRank(rank)).toList();
		var set = sets.get(random.nextInt(sets.size()));
		int slot = random.nextInt(set.items.length);
		return set.getItem(slot, rank);
	}
}
