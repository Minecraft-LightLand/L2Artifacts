package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseArtifact extends RankedItem {

	public static final String KEY = "ArtifactData", UPGRADE = "Upgrade";

	public static void upgrade(ItemStack stack, int exp, RandomSource random) {
		ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag(KEY);
		if (tag.isPresent()) {
			ArtifactStats stats = TagCodec.fromTag(tag.getOrCreate(), ArtifactStats.class);
			assert stats != null;
			stats.addExp(exp, random);
			CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
			assert newTag != null;
			tag.setTag(newTag);
		}
	}

	public static Optional<ArtifactStats> getStats(ItemStack stack) {
		return CuriosApi.getCurio(stack).filter(e -> e instanceof ArtifactCurioCap)
				.flatMap(e -> ((ArtifactCurioCap) e).getStats());
	}

	public static Optional<Upgrade> getUpgrade(ItemStack stack) {
		ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag(UPGRADE);
		if (tag.isPresent()) {
			return Optional.ofNullable(TagCodec.fromTag(tag.getOrCreate(), Upgrade.class));
		}
		return Optional.empty();
	}

	public static ItemStack setUpgrade(ItemStack stack, Upgrade upgrade) {
		CompoundTag tag = TagCodec.toTag(new CompoundTag(), upgrade);
		if (tag != null)
			ItemCompoundTag.of(stack).getSubTag(UPGRADE).setTag(tag);
		return stack;
	}

	public final Supplier<ArtifactSet> set;
	public final Supplier<ArtifactSlot> slot;

	public BaseArtifact(Properties properties, Supplier<ArtifactSet> set, Supplier<ArtifactSlot> slot, int rank) {
		super(properties.stacksTo(1), rank);
		this.set = set;
		this.slot = slot;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		return resolve(stack, level.isClientSide(), player.getRandom());
	}

	public InteractionResultHolder<ItemStack> resolve(ItemStack stack, boolean isClient, RandomSource random) {
		ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag(KEY);
		Upgrade upgrade = getUpgrade(stack).orElse(new Upgrade());
		if (!tag.isPresent()) {
			if (!isClient) {
				ArtifactStats stats = ArtifactStats.generate(slot.get(), rank, upgrade, random);
				CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
				assert newTag != null;
				tag.setTag(newTag);
				setUpgrade(stack, upgrade);
			}
			return InteractionResultHolder.success(stack);
		} else {
			Optional<ArtifactStats> opt = getStats(stack);
			if (opt.isPresent()) {
				ArtifactStats stats = opt.get();
				if (stats.level > stats.old_level) {
					if (!isClient) {
						for (int i = stats.old_level + 1; i <= stats.level; i++) {
							ArtifactUpgradeManager.onUpgrade(i, upgrade, random);
						}
						stats.old_level = stats.level;
						CompoundTag newTag = TagCodec.toTag(new CompoundTag(), stats);
						assert newTag != null;
						tag.setTag(newTag);
						setUpgrade(stack, upgrade);
					}
					return InteractionResultHolder.success(stack);
				}
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (level == null || !level.isClientSide()) return;
		boolean shift = Screen.hasShiftDown();
		if (Proxy.getClientPlayer() != null) {
			ItemCompoundTag tag = ItemCompoundTag.of(stack).getSubTag(KEY);
			if (!tag.isPresent()) {
				list.add(LangData.RAW_ARTIFACT.get());
			} else {
				getStats(stack).ifPresent(stats -> {
					boolean max = stats.level == ArtifactUpgradeManager.getMaxLevel(stats.rank);
					list.add(LangData.ARTIFACT_LEVEL.get(stats.level).withStyle(max ? ChatFormatting.GOLD : ChatFormatting.WHITE));
					if (stats.level < ArtifactUpgradeManager.getMaxLevel(stats.rank)) {
						if (shift)
							list.add(LangData.ARTIFACT_EXP.get(stats.exp, ArtifactUpgradeManager.getExpForLevel(stats.rank, stats.level)));
					}
					if (stats.level > stats.old_level) {
						list.add(LangData.UPGRADE.get());
					} else if (!shift) {
						list.add(LangData.MAIN_STAT.get());
						list.add(stats.main_stat.getTooltip());
						if (!stats.sub_stats.isEmpty()) {
							list.add(LangData.SUB_STAT.get());
							for (StatEntry ent : stats.sub_stats) {
								list.add(ent.getTooltip());
							}
						}
					}
				});
			}
			list.addAll(set.get().getAllDescs(stack, shift));
			if (!shift)
				list.add(LangData.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(rank, getStats(stack).orElse(null))));
		}
		super.appendHoverText(stack, level, list, flag);
		if (!shift) {
			list.add(LangData.SHIFT_TEXT.get());
		}
	}

}
