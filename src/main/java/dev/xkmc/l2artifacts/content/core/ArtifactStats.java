package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.config.WeightedLottery;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public record ArtifactStats(
		ArtifactSlot slot, int rank, int level, int exp, int old_level,
		StatEntry main_stat, ArrayList<StatEntry> sub_stats
) {

	public static ArtifactStats.Mutable of(ArtifactSlot slot, int rank) {
		return new Mutable(new ArtifactStats(slot, rank, 0, 0, 0, null, new ArrayList<>()));
	}

	public static ArtifactStats generate(RegistryAccess access, ArtifactSlot slot, int rank, Upgrade.Mutable upgrade, RandomSource random) {
		var ans = ArtifactStats.of(slot, rank);
		ans.generate(access, upgrade, random);
		return ans.immutable();
	}

	public Multimap<Holder<Attribute>, AttributeModifier> buildAttributes(ResourceLocation base) {
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
		main_stat.toModifier(builder, base);
		for (StatEntry ent : sub_stats) {
			ent.toModifier(builder, base);
		}
		return builder.build();
	}

	public ArtifactStats addExp(int gain) {
		int nexp = exp + gain;
		int nlevel = level;
		int max_level = ArtifactUpgradeManager.getMaxLevel(rank);
		while (true) {
			if (nlevel >= max_level) {
				break;
			}
			int max_exp = ArtifactUpgradeManager.getExpForLevel(rank, nlevel);
			if (nexp < max_exp) {
				break;
			}
			nexp -= max_exp;
			nlevel++;
		}
		if (nlevel == max_level) {
			nexp = 0;
		}
		return new ArtifactStats(slot, rank, nlevel, nexp, old_level, main_stat, sub_stats);
	}

	public ArtifactStats upgrade(Upgrade.Mutable upgrade, RandomSource random) {
		var mutable = new Mutable(this);
		for (int i = old_level + 1; i <= level; i++) {
			mutable.onUpgrade(i, upgrade, random);
		}
		return mutable.flush();
	}

	public boolean containsKey(Holder<StatType> astat) {
		if (main_stat.type().equals(astat)) return true;
		for (var e : sub_stats) {
			if (e.type().equals(astat)) return true;
		}
		return false;
	}

	@Nullable
	public StatEntry get(Holder<StatType> astat) {
		if (main_stat.type().equals(astat)) return main_stat;
		for (var e : sub_stats) {
			if (e.type().equals(astat)) return e;
		}
		return null;
	}

	public static class Mutable {

		private final ArtifactStats ref;
		private final ArrayList<StatEntry.Mutable> sub_stats;
		private final Map<Holder<StatType>, StatEntry.Mutable> map;
		private StatEntry.Mutable main_stat;

		private Mutable(ArtifactStats self) {
			this.ref = self;
			this.main_stat = ref.main_stat == null ? null : ref.main_stat.mutable();
			this.sub_stats = new ArrayList<>(self.sub_stats.stream()
					.map(StatEntry::mutable).toList());
			map = new LinkedHashMap<>();
			if (main_stat != null)
				map.put(main_stat.type(), main_stat);
			for (var ent : sub_stats) {
				map.put(ent.type(), ent);
			}
		}

		private void add(StatEntry entry) {
			if (map.containsKey(entry.type()))
				return;
			var m = entry.mutable();
			if (main_stat == null) {
				main_stat = m;
			} else {
				sub_stats.add(m);
			}
			map.put(entry.type(), m);
		}

		public void add(Holder<StatType> type, double value) {
			if (map.containsKey(type)) {
				map.get(type).addMultiplier(value);
			} else {
				add(new StatEntry(type, value));
			}
		}

		private void generate(RegistryAccess access, Upgrade.Mutable upgrade, RandomSource random) {
			var main_list = new WeightedLottery(access, random, true);
			var main = main_list.poll();
			var sub_list = new WeightedLottery(access, random, false);
			sub_list.remove(main);
			add(main, main.value().getInitialValue(random, upgrade.removeMain()));
			int roll = ref.rank() - 1;
			for (int i = 0; i < roll; i++) {
				if (sub_list.isEmpty()) break;
				Holder<StatType> sub = upgrade.removeStat();
				if (sub != null) sub_list.remove(sub);
				else sub = sub_list.poll();
				add(sub, sub.value().getSubValue(random, upgrade.removeSub()));
			}
		}

		private void onUpgrade(int lv, Upgrade.Mutable upgrade, RandomSource random) {
			int gate = ArtifactConfig.SERVER.levelPerSubStat.get();
			if (main_stat != null) {
				add(main_stat.type(), main_stat.type().value().getMainValue(random, upgrade.removeMain()));
			}
			if (lv % gate == 0 && !sub_stats.isEmpty()) {
				StatEntry.Mutable substat = upgrade.selectAmong(sub_stats);
				if (substat == null) {
					substat = sub_stats.get(random.nextInt(sub_stats.size()));
				}
				add(substat.type(), substat.type().value().getSubValue(random, upgrade.removeSub()));
			}
		}

		public ArtifactStats immutable() {
			var main = main_stat.immutable();
			var sub = new ArrayList<>(sub_stats.stream().map(StatEntry.Mutable::immutable).toList());
			return new ArtifactStats(ref.slot, ref.rank, ref.level, ref.exp, ref.old_level, main, sub);
		}

		private ArtifactStats flush() {
			var main = main_stat.immutable();
			var sub = new ArrayList<>(sub_stats.stream().map(StatEntry.Mutable::immutable).toList());
			return new ArtifactStats(ref.slot, ref.rank, ref.level, ref.exp, ref.level, main, sub);
		}

	}

}
