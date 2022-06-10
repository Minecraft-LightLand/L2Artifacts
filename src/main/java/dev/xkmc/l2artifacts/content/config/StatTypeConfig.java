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
		public double base_low, base_high, main_low, main_high, sub_low, sub_high;

		public int count = 1;

		public void set(Entry t1) {
			int total = count + t1.count;
			base_low = (base_low * count + t1.base_low * t1.count) / total;
			base_high = (base_high * count + t1.base_high * t1.count) / total;
			main_low = (main_low * count + t1.main_low * t1.count) / total;
			main_high = (main_high * count + t1.main_high * t1.count) / total;
			sub_low = (sub_low * count + t1.sub_low * t1.count) / total;
			sub_high = (sub_high * count + t1.sub_high * t1.count) / total;
			count = total;
		}

	}

}
