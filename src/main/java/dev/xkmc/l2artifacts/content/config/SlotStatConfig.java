package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.SerialClass;

import java.util.ArrayList;
import java.util.HashMap;

@SerialClass
public class SlotStatConfig extends BaseConfig {

	public static SlotStatConfig getInstance() {
		return NetworkManager.SLOT_STATS.getMerged();
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatTypeHolder>> available_main_stats = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatTypeHolder>> available_sub_stats = new HashMap<>();

}
