package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.init.L2LibReg;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BaseArtifact extends RankedItem implements ICurioItem {

	public static void upgrade(ItemStack stack, int exp) {
		var stats = ArtifactItems.STATS.get(stack);
		if (stats == null) return;
		ArtifactItems.STATS.set(stack, stats.addExp(exp));
	}

	public static Optional<ArtifactStats> getStats(ItemStack stack) {
		return Optional.ofNullable(ArtifactItems.STATS.get(stack));
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
		var access = Proxy.getRegistryAccess();
		if (access != null && optStats.isEmpty()) {
			if (!isClient) {
				var mu = upgrade.mutable();
				ArtifactStats stats = ArtifactStats.generate(access, slot.get(), rank, mu, random);
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

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext ctx, ResourceLocation id, ItemStack stack) {
		var stats = getStats(stack);
		if (stats.isPresent()) {
			return stats.get().buildAttributes(id);
		}
		return ICurioItem.super.getAttributeModifiers(ctx, id, stack);
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		try {
			if (stack.getItem() instanceof BaseArtifact base) {
				base.set.get().tick(slotContext);
			}
		} catch (Exception e) {
			if (slotContext.entity() instanceof Player player) {
				L2LibReg.CONDITIONAL.type().getOrCreate(player).data.entrySet().removeIf(x -> x.getKey().type().equals(L2Artifacts.MODID));
				L2Artifacts.LOGGER.error("Player " + player + " has invalid artifact data for " + stack.getItem() + ". This could be a bug.");
			}
		}
	}

}
