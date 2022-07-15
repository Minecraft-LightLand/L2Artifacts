package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2library.serial.config.ConfigMerger;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.PacketHandlerWithConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public enum NetworkManager {
	ARTIFACT_SETS, SLOT_STATS, STAT_TYPES, LINEAR;

	public String getID() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(L2Artifacts.MODID, "main"), 1, "artifact_config"
	);

	public <T extends BaseConfig> T getMerged() {
		return HANDLER.getCachedConfig(getID());
	}

	public static void register() {
		HANDLER.addCachedConfig(ARTIFACT_SETS.getID(), s -> {
			List<ArtifactSetConfig> configs = s.map(e -> (ArtifactSetConfig) e.getValue()).toList();
			HashMap<ArtifactSet, ArrayList<ArtifactSetConfig.Entry>> map = BaseConfig.collectMap(configs, e -> e.map, ArrayList::new, ArrayList::addAll);
			map.values().forEach(e -> e.sort(null));
			ArtifactSetConfig ans = new ArtifactSetConfig();
			map.forEach((k, v) -> v.forEach(e -> e.validate(k)));
			ans.map = map;
			return ans;
		});

		HANDLER.addCachedConfig(SLOT_STATS.getID(), new ConfigMerger<>(SlotStatConfig.class));

		HANDLER.addCachedConfig(STAT_TYPES.getID(), s -> {
			List<StatTypeConfig> configs = s.map(e -> (StatTypeConfig) e.getValue()).toList();
			HashMap<ArtifactStatType, StatTypeConfig.Entry> main = BaseConfig.collectMap(configs, e -> e.stats, StatTypeConfig::getEmpty, StatTypeConfig.Entry::set);
			StatTypeConfig ans = new StatTypeConfig();
			ans.stats = main;
			return ans;
		});

		HANDLER.addCachedConfig(LINEAR.getID(), new ConfigMerger<>(LinearFuncConfig.class));
	}

}
