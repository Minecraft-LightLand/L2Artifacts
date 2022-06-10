package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;

import java.util.HashMap;
import java.util.List;

@SerialClass
public class StatTypeConfig extends BaseConfig {

	public static StatTypeConfig cache;

	public static StatTypeConfig getInstance() {
		if (cache != null) return cache;
		List<StatTypeConfig> configs = NetworkManager.getConfigs("stat_types").map(e -> (StatTypeConfig) e.getValue()).toList();
		HashMap<ArtifactStatType, Entry> main = BaseConfig.collectMap(configs, e -> e.stats, Entry::new, Entry::set);
		StatTypeConfig ans = new StatTypeConfig();
		ans.stats = main;
		cache = ans;
		return ans;
	}

	@SerialClass.SerialField
	public HashMap<ArtifactStatType, Entry> stats = new HashMap<>();

	@SerialClass
	public static class Entry {

		@SerialClass.SerialField
		public double low, high;

		public void set(Entry t1) {
			low = t1.low;
			high = t1.high;
		}

	}

}
