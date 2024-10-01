package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.config.SetGroup;
import dev.xkmc.l2artifacts.content.core.RankedItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RandomArtifactItem extends RankedItem {

	public RandomArtifactItem(Properties props, int rank) {
		super(props, rank);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.awardStat(Stats.ITEM_USED.get(this));
		if (!level.isClientSide) {
			player.getInventory().placeItemBackInInventory(getRandomArtifact(stack, rank, player.getRandom()));
		}
		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	public static ItemStack setList(int rank, Collection<SetEntry<?>> sets) {
		ItemStack stack = ArtifactItems.RANDOM[rank - 1].asStack();
		return ArtifactItems.GROUP.set(stack, SetGroup.of(rank, sets));
	}

	@Nullable
	private static Collection<SetEntry<?>> getList(ItemStack stack, int rank) {
		var group = ArtifactItems.GROUP.get(stack);
		if (group == null) return null;
		return group.getSets(rank, false);
	}

	public static ItemStack getRandomArtifact(ItemStack stack, int rank, RandomSource random) {
		var list = getList(stack, rank);
		if (list == null) list = L2Artifacts.REGISTRATE.SET_LIST;
		var sets = list.stream().filter(e -> e.hasRank(rank))
				.flatMap(e -> Arrays.stream(e.items)).toList();
		var arr = sets.get(random.nextInt(sets.size()));
		return arr[rank - arr[0].get().rank].asStack();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var sets = getList(stack, rank);
		if (sets == null) {
			list.add(ArtifactLang.LOOT_POOL_ALL.get());
		} else {
			list.add(ArtifactLang.LOOT_POOL.get());
			for (var e : sets) {
				list.add(e.get().getDesc().withStyle(ChatFormatting.GRAY));
			}
		}
	}
}
