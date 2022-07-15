package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.upgrades.*;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CraftEvents {

	@SubscribeEvent
	public static void onAnvilCraft(AnvilUpdateEvent event) {
		ItemStack stack = event.getLeft();
		if (stack.getItem() instanceof BaseArtifact) {
			ItemStack fodder = event.getRight();
			if (fodder.getItem() instanceof ExpItem) {
				upgradeExp(event);
			}
			if (fodder.getItem() instanceof BaseArtifact) {
				upgradeExp(event);
			}
			if (fodder.getItem() instanceof StatContainerItem) {
				upgradeEnhance(event);
			}
		}
		if (stack.getItem() instanceof StatContainerItem) {
			captureStat(event);
		}
	}

	private static void upgradeExp(AnvilUpdateEvent event) {
		ItemStack stack = event.getLeft();
		ItemStack fodder = event.getRight();
		int val = 0;
		if (fodder.getItem() instanceof BaseArtifact base) {
			val = ArtifactUpgradeManager.getExpForConversion(base.rank, BaseArtifact.getStats(fodder).orElse(null));
		} else if (fodder.getItem() instanceof ExpItem exp) {
			val = ArtifactUpgradeManager.getExpForConversion(exp.rank, null);
		}
		if (val > 0 && stack.getItem() instanceof BaseArtifact artifact) {
			Optional<ArtifactStats> opt = BaseArtifact.getStats(stack);
			if (opt.isPresent()) {
				ItemStack new_stack = stack.copy();
				BaseArtifact.upgrade(new_stack, val, event.getPlayer().getRandom());
				event.setOutput(new_stack);
				event.setMaterialCost(1);
				event.setCost((int) Math.ceil(Math.log(val) * artifact.rank));
			}
		}
	}

	private static void upgradeEnhance(AnvilUpdateEvent event) {
		ItemStack stack = event.getLeft();
		ItemStack fodder = event.getRight();
		if (stack.getItem() instanceof BaseArtifact artifact &&
				fodder.getItem() instanceof UpgradeEnhanceItem enhance &&
				enhance.rank == artifact.rank) {
			if (enhance instanceof StatContainerItem) {
				StatContainerItem.getType(fodder).ifPresent(stat -> {
					if (shouldUpgradeSetSubStat(event, stat)) {
						Upgrade upgrade = BaseArtifact.getUpgrade(stack).orElse(new Upgrade());
						upgrade.stats.add(stat);

						ItemStack result = stack.copy();
						event.setOutput(BaseArtifact.setUpgrade(result, upgrade));
						event.setMaterialCost(1);
						event.setCost(enhance.rank);
					}
				});
			}
			if (enhance instanceof UpgradeBoostItem boost) {
				shouldUpgradeBoost(event, boost.type).ifPresent(upgrade -> {
					ItemStack result = stack.copy();
					event.setOutput(BaseArtifact.setUpgrade(result, upgrade));
					event.setMaterialCost(1);
					event.setCost(enhance.rank);
				});
			}
		}
	}

	private static boolean shouldUpgradeSetSubStat(AnvilUpdateEvent event, ArtifactStatType stat) {
		ItemStack stack = event.getLeft();
		BaseArtifact artifact = (BaseArtifact) stack.getItem();
		Upgrade upgrade = BaseArtifact.getUpgrade(stack).orElse(new Upgrade());
		int maxLvl = ArtifactUpgradeManager.getMaxLevel(artifact.rank);
		int lvl = BaseArtifact.getStats(stack).map(e -> e.old_level).orElse(-1);
		int pre = lvl == -1 ? artifact.rank - 1 : 0;
		int gate = ModConfig.COMMON.levelPerSubStat.get();
		if (maxLvl / gate - lvl / gate + pre <= upgrade.stats.size())
			return false;
		return BaseArtifact.getStats(stack).map(stats -> {
			for (StatEntry entry : stats.sub_stats) {
				if (entry.type == stat)
					return true;
			}
			return false;
		}).orElseGet(() -> {
			var list = SlotStatConfig.getInstance().available_sub_stats.get(artifact.slot.get());
			if (!list.contains(stat))
				return false;
			Set<ArtifactStatType> set = new HashSet<>(upgrade.stats);
			set.add(stat);
			return set.size() <= artifact.rank - 1;
		});
	}

	private static Optional<Upgrade> shouldUpgradeBoost(AnvilUpdateEvent event, Upgrade.Type type) {
		ItemStack stack = event.getLeft();
		BaseArtifact artifact = (BaseArtifact) stack.getItem();
		int maxLvl = ArtifactUpgradeManager.getMaxLevel(artifact.rank);
		int lvl = BaseArtifact.getStats(stack).map(e -> e.old_level).orElse(-1);
		Upgrade upgrade = BaseArtifact.getUpgrade(stack).orElse(new Upgrade());
		if (type == Upgrade.Type.BOOST_MAIN_STAT) {
			if (maxLvl - lvl > upgrade.main) {
				upgrade.main++;
				return Optional.of(upgrade);
			}
		} else {
			int pre = lvl == -1 ? artifact.rank - 1 : 0;
			int gate = ModConfig.COMMON.levelPerSubStat.get();
			if (maxLvl / gate - lvl / gate + pre > upgrade.sub) {
				upgrade.sub++;
				return Optional.of(upgrade);
			}
		}
		return Optional.empty();
	}

	private static void captureStat(AnvilUpdateEvent event) {
		ItemStack stack = event.getLeft();
		ItemStack fodder = event.getRight();
		if (stack.getItem() instanceof StatContainerItem cont &&
				fodder.getItem() instanceof BaseArtifact artifact &&
				cont.rank == artifact.rank &&
				StatContainerItem.getType(stack).isEmpty()) {
			BaseArtifact.getStats(fodder).ifPresent(stat -> {
				ItemStack result = stack.copy();
				event.setOutput(StatContainerItem.setStat(result, stat.main_stat.type));
				event.setMaterialCost(1);
				event.setCost(cont.rank);
			});
		}
	}


}
