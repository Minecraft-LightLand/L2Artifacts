package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactDatapackRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ConfigGen extends ConfigDataProvider {

	private final CompletableFuture<HolderLookup.Provider> registries;

	public ConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> registries) {
		super(generator, "Artifact Config");
		this.registries = registries;
	}

	@Override
	public void add(Collector map) {
		registries.thenAccept(registries -> {
			// Slot Stat Config
			{
				ArrayList<ArtifactStatTypeHolder> all = new ArrayList<>();
				{
					all.add(ArtifactDatapackRegistry.HEALTH_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.ARMOR_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.TOUGH_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.ATK_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.ATK_MULT.get(registries));
					all.add(ArtifactDatapackRegistry.CR_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.CD_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.REACH_ADD.get(registries));
					all.add(ArtifactDatapackRegistry.ATK_SPEED_MULT.get(registries));
					all.add(ArtifactDatapackRegistry.SPEED_MULT.get(registries));
					all.add(ArtifactDatapackRegistry.BOW_ADD.get(registries));
				}
				{
					ArrayList<ArtifactStatTypeHolder> list = new ArrayList<>();
					list.add(ArtifactDatapackRegistry.HEALTH_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ARMOR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.SPEED_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.CR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.TOUGH_ADD.get(registries));
					addSlotStat(map, ArtifactTypeRegistry.SLOT_BODY.get(), list, all);
				}
				{
					ArrayList<ArtifactStatTypeHolder> list = new ArrayList<>();
					list.add(ArtifactDatapackRegistry.ATK_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.BOW_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.CD_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.REACH_ADD.get(registries));
					addSlotStat(map, ArtifactTypeRegistry.SLOT_BRACELET.get(), list, all);
				}
				{
					ArrayList<ArtifactStatTypeHolder> list = new ArrayList<>();
					list.add(ArtifactDatapackRegistry.HEALTH_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ARMOR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.BOW_ADD.get(registries));

					list.add(ArtifactDatapackRegistry.CR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.CD_ADD.get(registries));
					addSlotStat(map, ArtifactTypeRegistry.SLOT_NECKLACE.get(), list, all);
				}
				{
					ArrayList<ArtifactStatTypeHolder> list = new ArrayList<>();
					list.add(ArtifactDatapackRegistry.HEALTH_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ARMOR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.BOW_ADD.get(registries));

					list.add(ArtifactDatapackRegistry.ATK_SPEED_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.SPEED_MULT.get(registries));
					addSlotStat(map, ArtifactTypeRegistry.SLOT_BELT.get(), list, all);
				}
				{
					ArrayList<ArtifactStatTypeHolder> list = new ArrayList<>();
					list.add(ArtifactDatapackRegistry.HEALTH_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ARMOR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.BOW_ADD.get(registries));

					list.add(ArtifactDatapackRegistry.TOUGH_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.REACH_ADD.get(registries));

					list.add(ArtifactDatapackRegistry.CR_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.CD_ADD.get(registries));
					list.add(ArtifactDatapackRegistry.ATK_SPEED_MULT.get(registries));
					list.add(ArtifactDatapackRegistry.SPEED_MULT.get(registries));

					addSlotStat(map, ArtifactTypeRegistry.SLOT_HEAD.get(), list, all);
				}
			}

			// Stat Type Config

			addStatType(map, ArtifactDatapackRegistry.HEALTH_ADD.get(registries), 0.4);
			addStatType(map, ArtifactDatapackRegistry.ARMOR_ADD.get(registries), 0.4);
			addStatType(map, ArtifactDatapackRegistry.TOUGH_ADD.get(registries), 0.2);
			addStatType(map, ArtifactDatapackRegistry.ATK_ADD.get(registries), 0.4);
			addStatType(map, ArtifactDatapackRegistry.ATK_MULT.get(registries), 0.02);
			addStatType(map, ArtifactDatapackRegistry.CR_ADD.get(registries), 0.01);
			addStatType(map, ArtifactDatapackRegistry.CD_ADD.get(registries), 0.02);
			addStatType(map, ArtifactDatapackRegistry.REACH_ADD.get(registries), 0.02);
			addStatType(map, ArtifactDatapackRegistry.SPEED_MULT.get(registries), 0.01);
			addStatType(map, ArtifactDatapackRegistry.ATK_SPEED_MULT.get(registries), 0.01);
			addStatType(map, ArtifactDatapackRegistry.BOW_ADD.get(registries), 0.02);

			// Set Effect Config

			for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
				addArtifactSet(map, set.get(), set.builder);
			}

			// linear function handle

			for (var key : L2Artifacts.REGISTRATE.LINEAR_LIST.keySet()) {
				LinearFuncConfig config = new LinearFuncConfig();
				var list = L2Artifacts.REGISTRATE.LINEAR_LIST.get(key);
				for (var entry : list) {
					config.map.put(entry.get(), new LinearFuncConfig.Entry(entry.base, entry.slope));
				}
				map.add(NetworkManager.LINEAR, key, config);
			}
		});
	}

	public static void addSlotStat(Collector map, ArtifactSlot slot, ArrayList<ArtifactStatTypeHolder> main, ArrayList<ArtifactStatTypeHolder> sub) {
		SlotStatConfig config = new SlotStatConfig();
		ResourceLocation rl = Objects.requireNonNull(slot.getRegistryName());
		config.available_main_stats.put(slot, main);
		config.available_sub_stats.put(slot, sub);
		map.add(NetworkManager.SLOT_STATS, rl, config);
	}

	private static void addStatType(Collector map, ArtifactStatTypeHolder type, double base) {
		StatTypeConfig config = new StatTypeConfig();
		ResourceLocation rl = Objects.requireNonNull(type.getID());
		config.stats.put(type, genEntry(base, 0.2, 2));
		map.add(NetworkManager.STAT_TYPES, rl, config);
	}

	private static void addArtifactSet(Collector map, ArtifactSet set, Consumer<ArtifactSetConfig.SetBuilder> builder) {
		ResourceLocation rl = Objects.requireNonNull(set.getRegistryName());
		map.add(NetworkManager.ARTIFACT_SETS, rl, ArtifactSetConfig.construct(set, builder));
	}

	private static StatTypeConfig.Entry genEntry(double base, double sub, double factor) {
		StatTypeConfig.Entry entry = new StatTypeConfig.Entry();
		entry.base = base;
		entry.base_low = 1;
		entry.base_high = factor;
		entry.main_low = sub;
		entry.main_high = sub * factor;
		entry.sub_low = sub;
		entry.sub_high = sub * factor;
		return entry;
	}


}