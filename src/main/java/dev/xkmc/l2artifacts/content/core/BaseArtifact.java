package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class BaseArtifact extends Item {

	public static final String KEY = "ArtifactData";

	protected static Rarity getRarity(int rank) {
		return rank <= 2 ? Rarity.UNCOMMON : rank <= 4 ? Rarity.RARE : Rarity.EPIC;
	}

	public static void upgrade(ItemStack stack, int exp, Random random) {
		if (stack.getTag() != null && stack.getTag().contains(KEY)) {
			ArtifactStats stats = TagCodec.fromTag(stack.getTag().getCompound(KEY), ArtifactStats.class);
			stats.addExp(exp, random);
			stack.getTag().put(KEY, TagCodec.toTag(new CompoundTag(), stats));
		}
	}

	public static Optional<ArtifactStats> getStats(ItemStack stack) {
		return CuriosApi.getCuriosHelper().getCurio(stack).filter(e -> e instanceof ArtifactCurioCap)
				.flatMap(e -> ((ArtifactCurioCap) e).getStats());
	}


	public final Supplier<ArtifactSlot> slot;
	public final int rank;

	public BaseArtifact(Properties properties, Supplier<ArtifactSlot> slot, int rank) {
		super(properties.stacksTo(1).rarity(getRarity(rank)));
		this.slot = slot;
		this.rank = rank;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getTag() == null || !stack.getTag().contains(KEY)) {
			if (!level.isClientSide()) {
				ArtifactStats stats = new ArtifactStats(slot.get(), rank, level.random);
				stack.getOrCreateTag().put(KEY, TagCodec.toTag(new CompoundTag(), stats));
			}
			return InteractionResultHolder.success(stack);
		} else {
			Optional<ArtifactStats> opt = getStats(stack);
			if (opt.isPresent()) {
				ArtifactStats stats = opt.get();
				if (stats.level > stats.old_level) {
					for (int i = stats.old_level + 1; i <= stats.level; i++) {
						ArtifactUpgradeManager.onUpgrade(stats, i, level.random);
					}
					stats.old_level = stats.level;
					stack.getOrCreateTag().put(KEY, TagCodec.toTag(new CompoundTag(), stats));
					return InteractionResultHolder.success(stack);
				}
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (stack.getTag() == null || !stack.getTag().contains(KEY)) {
			list.add(LangData.RAW_ARTIFACT.get());
		} else {
			getStats(stack).ifPresent(stats -> {
				list.add(LangData.ARTIFACT_LEVEL.get(stats.level));
				if (stats.level < ArtifactUpgradeManager.getMaxLevel(stats.rank)) {
					list.add(LangData.ARTIFACT_EXP.get(stats.exp, ArtifactUpgradeManager.getExpForLevel(stats.rank, stats.level)));
				}
				if (stats.level > stats.old_level) {
					list.add(LangData.UPGRADE.get());
				} else {
					list.add(LangData.MAIN_STAT.get());
					list.add(stats.main_stat.getTooltip());
					if (stats.sub_stats.size() > 0) {
						list.add(LangData.SUB_STAT.get());
						for (StatEntry ent : stats.sub_stats) {
							list.add(ent.getTooltip());
						}
					}
				}
			});
		}
		list.add(LangData.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(rank, getStats(stack).orElse(null))));
		super.appendHoverText(stack, level, list, flag);
	}
}
