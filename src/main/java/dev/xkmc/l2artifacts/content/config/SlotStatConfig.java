package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.BaseConfig;

import java.util.ArrayList;
import java.util.HashMap;

@SerialClass
public class SlotStatConfig extends BaseConfig {

	public static SlotStatConfig getInstance() {
		return NetworkManager.SLOT_STATS.getMerged();
	}

	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> available_main_stats = new HashMap<>();

	@SerialClass.SerialField
	public HashMap<ArtifactSlot, ArrayList<ArtifactStatType>> available_sub_stats = new HashMap<>();

}
