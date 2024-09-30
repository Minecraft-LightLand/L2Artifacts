package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2core.serial.config.BaseConfig;
import dev.xkmc.l2core.serial.config.CollectType;
import dev.xkmc.l2core.serial.config.ConfigCollect;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;

import java.util.ArrayList;
import java.util.HashMap;

@SerialClass
public class SlotStatConfig extends BaseConfig {

	public static SlotStatConfig getInstance() {
		return NetworkManager.SLOT_STATS.getMerged();
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public HashMap<ArtifactSlot, ArrayList<Holder<StatType>>> available_main_stats = new HashMap<>();

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public HashMap<ArtifactSlot, ArrayList<Holder<StatType>>> available_sub_stats = new HashMap<>();

}
