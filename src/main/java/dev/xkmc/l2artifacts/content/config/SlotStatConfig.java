package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SerialClass
public class SlotStatConfig extends BaseConfig {

	public static SlotStatConfig cache;

	public static SlotStatConfig getInstance() {
		if (cache != null) return cache;
		List<SlotStatConfig> configs = NetworkManager.getConfigs("slot_stats").map(e -> (SlotStatConfig) e.getValue()).toList();
		HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> main = BaseConfig.collectMap(configs, e -> e.available_main_stats, ArrayList::new, ArrayList::addAll);
		HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> sub = BaseConfig.collectMap(configs, e -> e.available_sub_stats, ArrayList::new, ArrayList::addAll);
		SlotStatConfig ans = new SlotStatConfig();
		ans.available_main_stats = main;
		ans.available_sub_stats = sub;
		cache = ans;
		return ans;
	}

	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> available_main_stats = new HashMap<>();

	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> available_sub_stats = new HashMap<>();

}
