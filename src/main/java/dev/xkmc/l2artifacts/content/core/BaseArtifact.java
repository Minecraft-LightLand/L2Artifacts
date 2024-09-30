package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseArtifact extends RankedItem {

	public static void upgrade(ItemStack stack, int exp) {
		var stats = ArtifactItems.STATS.get(stack);
		if (stats == null) return;
		ArtifactItems.STATS.set(stack, stats.addExp(exp));
	}

	public static Optional<ArtifactStats> getStats(ItemStack stack) {
		return CuriosApi.getCurio(stack).filter(e -> e instanceof ArtifactCurioCap)
				.flatMap(e -> ((ArtifactCurioCap) e).getStats());
	}

	public static Optional<Upgrade> getUpgrade(ItemStack stack) {
		return Optional.ofNullable(ArtifactItems.UPGRADES.get(stack));
	}

	public static ItemStack complete(ItemStack stack, ArtifactStats stats, Upgrade.Mutable upgrade) {
		ArtifactItems.STATS.set(stack, stats);
		ArtifactItems.UPGRADES.set(stack, upgrade.immutable());
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
		var optStats = getStats(stack);
		Upgrade upgrade = getUpgrade(stack).orElse(Upgrade.EMPTY);
		if (optStats.isEmpty()) {
			if (!isClient) {
				var mu = upgrade.mutable();
				ArtifactStats stats = ArtifactStats.generate(slot.get(), rank, mu, random);
				complete(stack, stats, mu);
			}
			return InteractionResultHolder.success(stack);
		} else {
			ArtifactStats stats = optStats.get();
			if (stats.level() > stats.old_level()) {
				if (!isClient) {
					var mu = upgrade.mutable();
					stats = stats.upgrade(mu, random);
					complete(stack, stats, mu);
				}
				return InteractionResultHolder.success(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		boolean shift = flag.hasShiftDown();
		if (Proxy.getClientPlayer() != null) {
			var stats = getStats(stack);
			if (stats.isEmpty()) {
				list.add(ArtifactLang.RAW_ARTIFACT.get());
			} else {
				var s = stats.get();
				boolean max = s.level() == ArtifactUpgradeManager.getMaxLevel(s.rank());
				list.add(ArtifactLang.ARTIFACT_LEVEL.get(s.level()).withStyle(max ? ChatFormatting.GOLD : ChatFormatting.WHITE));
				if (s.level() < ArtifactUpgradeManager.getMaxLevel(s.rank())) {
					if (shift)
						list.add(ArtifactLang.ARTIFACT_EXP.get(s.exp(), ArtifactUpgradeManager.getExpForLevel(s.rank(), s.level())));
				}
				if (s.level() > s.old_level()) {
					list.add(ArtifactLang.UPGRADE.get());
				} else if (!shift) {
					list.add(ArtifactLang.MAIN_STAT.get());
					list.add(s.main_stat().getTooltip());
					if (!s.sub_stats().isEmpty()) {
						list.add(ArtifactLang.SUB_STAT.get());
						for (StatEntry ent : s.sub_stats()) {
							list.add(ent.getTooltip());
						}
					}
				}
			}
			list.addAll(set.get().getAllDescs(stack, shift));
			if (!shift)
				list.add(ArtifactLang.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(rank, getStats(stack).orElse(null))));
		}
		if (!shift) {
			list.add(ArtifactLang.SHIFT_TEXT.get());
		}
	}

}
