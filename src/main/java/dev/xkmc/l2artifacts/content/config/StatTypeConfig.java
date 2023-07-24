package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;

import java.util.HashMap;

@SerialClass
public class StatTypeConfig extends BaseConfig {

	public static StatTypeConfig getInstance() {
		return NetworkManager.STAT_TYPES.getMerged();
	}

	@ConfigCollect(CollectType.MAP_OVERWRITE)
	@SerialClass.SerialField
	public HashMap<ArtifactStatTypeHolder, Entry> stats = new HashMap<>();

	@SerialClass
	public static class Entry {

		@SerialClass.SerialField
		public double base, base_low, base_high, main_low, main_high, sub_low, sub_high;


	}

}
