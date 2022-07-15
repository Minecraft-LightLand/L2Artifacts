package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.BaseConfig;

import java.util.HashMap;

@SerialClass
public class StatTypeConfig extends BaseConfig {

	public static StatTypeConfig getInstance() {
		return NetworkManager.STAT_TYPES.getMerged();
	}

	public static Entry getEmpty() {
		Entry ans = new Entry();
		ans.count = 0;
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
