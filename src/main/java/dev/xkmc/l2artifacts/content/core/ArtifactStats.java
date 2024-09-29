package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2serial.serialization.marker.OnInject;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SerialClass
public class ArtifactStats {

	public static ArtifactStats generate(ArtifactSlot slot, int rank, Upgrade upgrade, RandomSource random) {
		ArtifactStats ans = new ArtifactStats(slot, rank);
		slot.generate(ans, upgrade, random);
		return ans;
	}

	@SerialField
	public ArtifactSlot slot;

	@SerialField
	public int rank, level, exp, old_level;

	@SerialField
	public StatEntry main_stat;

	@SerialField
	public ArrayList<StatEntry> sub_stats = new ArrayList<>();

	public final Map<ResourceLocation, StatEntry> map = new HashMap<>();

	@Deprecated
	public ArtifactStats() {

	}

	public ArtifactStats(ArtifactSlot slot, int rank) {
		this.slot = slot;
		this.rank = rank;
	}

	@OnInject
	public void onInject() {
		map.put(main_stat.type, main_stat);
		for (StatEntry ent : sub_stats) {
			map.put(ent.type, ent);
		}
	}

	public void add(StatEntry entry) {
		if (map.containsKey(entry.type))
			return;
		if (main_stat == null) {
			main_stat = entry;
		} else {
			sub_stats.add(entry);
		}
		map.put(entry.type, entry);
	}

	public void add(ResourceLocation type, double value) {
		if (map.containsKey(type)) {
			map.get(type).addMultiplier(value);
		} else {
			add(new StatEntry(slot, type, value));
		}
	}

	public Multimap<Attribute, AttributeModifier> buildAttributes(ResourceLocation uuidBase) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		for (StatEntry ent : map.values()) {
			ent.getType().getModifier(builder, ent, uuidBase.withSuffix("_" + ent.type.getPath()));
		}
		return builder.build();
	}

	public void addExp(int exp, RandomSource random) {
		this.exp += exp;
		int max_level = ArtifactUpgradeManager.getMaxLevel(rank);
		while (true) {
			if (this.level >= max_level) {
				break;
			}
			int max_exp = ArtifactUpgradeManager.getExpForLevel(rank, level);
			if (this.exp < max_exp) {
				break;
			}
			this.exp -= max_exp;
			this.level++;
		}
		if (this.level == max_level) {
			this.exp = 0;
		}
	}

}
