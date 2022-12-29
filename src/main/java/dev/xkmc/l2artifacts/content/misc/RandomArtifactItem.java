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

import java.util.Arrays;

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
		if (!level.isClientSide) {
			player.getInventory().placeItemBackInInventory(getRandomArtifact(rank, player.getRandom()));
		}
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

	public static ItemStack getRandomArtifact(int rank, RandomSource random) {
		var sets = L2Artifacts.REGISTRATE.SET_LIST.stream().filter(e -> e.hasRank(rank))
				.flatMap(e -> Arrays.stream(e.items)).toList();
		var arr = sets.get(random.nextInt(sets.size()));
		return arr[rank - arr[0].get().rank].asStack();
	}
}
