package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.network.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ConfigGen extends ConfigDataProvider {

	public ConfigGen(DataGenerator generator) {
		super(generator, "data/", "Artifact Config");
	}

	@Override
	public void add(Map<String, BaseConfig> map) {
		// Slot Stat Config
		{
			ArrayList<ArtifactStatType> all = new ArrayList<>();
			{
				all.add(ArtifactRegistry.HEALTH_ADD.get());
				all.add(ArtifactRegistry.ARMOR_ADD.get());
				all.add(ArtifactRegistry.TOUGH_ADD.get());
				all.add(ArtifactRegistry.ATK_ADD.get());
				all.add(ArtifactRegistry.ATK_MULT.get());
				all.add(ArtifactRegistry.CR_ADD.get());
				all.add(ArtifactRegistry.CD_ADD.get());
				all.add(ArtifactRegistry.REACH_ADD.get());
				all.add(ArtifactRegistry.ATK_SPEED_MULT.get());
				all.add(ArtifactRegistry.SPEED_MULT.get());
			}
			{
				ArrayList<ArtifactStatType> list = new ArrayList<>();
				list.add(ArtifactRegistry.HEALTH_ADD.get());
				list.add(ArtifactRegistry.ARMOR_ADD.get());

				list.add(ArtifactRegistry.TOUGH_ADD.get());
				addSlotStat(map, ArtifactRegistry.SLOT_BODY.get(), list, all);
			}
			{
				ArrayList<ArtifactStatType> list = new ArrayList<>();
				list.add(ArtifactRegistry.ATK_ADD.get());
				list.add(ArtifactRegistry.ATK_MULT.get());

				list.add(ArtifactRegistry.REACH_ADD.get());
				addSlotStat(map, ArtifactRegistry.SLOT_BRACELET.get(), list, all);
			}
			{
				ArrayList<ArtifactStatType> list = new ArrayList<>();
				list.add(ArtifactRegistry.HEALTH_ADD.get());
				list.add(ArtifactRegistry.ARMOR_ADD.get());
				list.add(ArtifactRegistry.ATK_ADD.get());
				list.add(ArtifactRegistry.ATK_MULT.get());

				list.add(ArtifactRegistry.CR_ADD.get());
				list.add(ArtifactRegistry.CD_ADD.get());
				addSlotStat(map, ArtifactRegistry.SLOT_NECKLACE.get(), list, all);
			}
			{
				ArrayList<ArtifactStatType> list = new ArrayList<>();
				list.add(ArtifactRegistry.HEALTH_ADD.get());
				list.add(ArtifactRegistry.ARMOR_ADD.get());
				list.add(ArtifactRegistry.ATK_ADD.get());
				list.add(ArtifactRegistry.ATK_MULT.get());

				list.add(ArtifactRegistry.ATK_SPEED_MULT.get());
				list.add(ArtifactRegistry.SPEED_MULT.get());
				addSlotStat(map, ArtifactRegistry.SLOT_BELT.get(), list, all);
			}
			{
				ArrayList<ArtifactStatType> list = new ArrayList<>();
				list.add(ArtifactRegistry.HEALTH_ADD.get());
				list.add(ArtifactRegistry.ARMOR_ADD.get());
				list.add(ArtifactRegistry.ATK_ADD.get());
				list.add(ArtifactRegistry.ATK_MULT.get());

				list.add(ArtifactRegistry.TOUGH_ADD.get());
				list.add(ArtifactRegistry.REACH_ADD.get());

				list.add(ArtifactRegistry.CR_ADD.get());
				list.add(ArtifactRegistry.CD_ADD.get());
				list.add(ArtifactRegistry.ATK_SPEED_MULT.get());
				list.add(ArtifactRegistry.SPEED_MULT.get());

				addSlotStat(map, ArtifactRegistry.SLOT_HEAD.get(), list, all);
			}
		}

		// Stat Type Config
		{
			addStatType(map, ArtifactRegistry.HEALTH_ADD.get(), 1);
			addStatType(map, ArtifactRegistry.ARMOR_ADD.get(), 1);
			addStatType(map, ArtifactRegistry.TOUGH_ADD.get(), 1);
			addStatType(map, ArtifactRegistry.ATK_ADD.get(), 1);
			addStatType(map, ArtifactRegistry.ATK_MULT.get(), 0.02);
			addStatType(map, ArtifactRegistry.CR_ADD.get(), 0.01);
			addStatType(map, ArtifactRegistry.CD_ADD.get(), 0.02);
			addStatType(map, ArtifactRegistry.REACH_ADD.get(), 0.02);
			addStatType(map, ArtifactRegistry.SPEED_MULT.get(), 0.02);
			addStatType(map, ArtifactRegistry.ATK_SPEED_MULT.get(), 0.02);
		}

		// Set Effect Config
		{
			addArtifactSet(map, ArtifactRegistry.SET_GAMBLER.get(), (c) -> c
					.add(3, ArtifactRegistry.EFF_GAMBLER_3.get())
					.add(5, ArtifactRegistry.EFF_GAMBLER_5.get()));

			addArtifactSet(map, ArtifactRegistry.SET_BERSERKER.get(), (c) -> c
					.add(3, ArtifactRegistry.EFF_BERSERKER_3.get())
					.add(5, ArtifactRegistry.EFF_BERSERKER_5.get()));

			addArtifactSet(map, ArtifactRegistry.SET_SAINT.get(), (c) -> c
					.add(3, ArtifactRegistry.EFF_SAINT_REDUCTION.get())
					.add(5, ArtifactRegistry.EFF_SAINT_RESTORATION.get()));

			addArtifactSet(map, ArtifactRegistry.SET_PERFECTION.get(), (c) -> c
					.add(2, ArtifactRegistry.EFF_PERFECTION_PROTECTION.get())
					.add(4, ArtifactRegistry.EFF_PERFECTION_ABSORPTION.get()));

			addArtifactSet(map, ArtifactRegistry.SET_DAMOCLES.get(), (c) -> c
					.add(1, ArtifactRegistry.EFF_DAMOCLES.get()));

			addArtifactSet(map, ArtifactRegistry.SET_PROTECTION.get(), (c) -> c
					.add(1, ArtifactRegistry.EFF_PROTECTION_RESISTANCE.get()));
		}
	}

	private static void addSlotStat(Map<String, BaseConfig> map, ArtifactSlot slot, ArrayList<ArtifactStatType> main, ArrayList<ArtifactStatType> sub) {
		SlotStatConfig config = new SlotStatConfig();
		ResourceLocation rl = Objects.requireNonNull(slot.getRegistryName());
		config.available_main_stats.put(slot, main);
		config.available_sub_stats.put(slot, sub);
		map.put(rl.getNamespace() + "/artifact_config/slot_stats/" + rl.getPath(), config);
	}

	private static void addStatType(Map<String, BaseConfig> map, ArtifactStatType type, double base) {
		StatTypeConfig config = new StatTypeConfig();
		ResourceLocation rl = Objects.requireNonNull(type.getRegistryName());
		config.stats.put(type, genEntry(base, 0.2, 2));
		map.put(rl.getNamespace() + "/artifact_config/stat_types/" + rl.getPath(), config);
	}

	private static void addArtifactSet(Map<String, BaseConfig> map, ArtifactSet set, Consumer<ArtifactSetConfig.SetBuilder> builder) {
		ResourceLocation rl = Objects.requireNonNull(set.getRegistryName());
		map.put(rl.getNamespace() + "/artifact_config/artifact_sets/" + rl.getPath(), ArtifactSetConfig.construct(set, builder));
	}

	private static StatTypeConfig.Entry genEntry(double base, double sub, double factor) {
		StatTypeConfig.Entry entry = new StatTypeConfig.Entry();
		entry.base_low = base;
		entry.base_high = base * factor;
		entry.main_low = base * sub;
		entry.main_high = base * sub * factor;
		entry.sub_low = base * sub;
		entry.sub_high = base * sub * factor;
		return entry;
	}


}