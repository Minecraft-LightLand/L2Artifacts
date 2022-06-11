package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SerialClass
public class ArtifactSetConfig extends BaseConfig {

	public static ArtifactSetConfig cache;

	public static ArtifactSetConfig getInstance() {
		if (cache != null) return cache;
		List<ArtifactSetConfig> configs = NetworkManager.getConfigs("artifact_sets").map(e -> (ArtifactSetConfig) e.getValue()).toList();
		HashMap<ArtifactSet, ArrayList<Entry>> map = BaseConfig.collectMap(configs, e -> e.map, ArrayList::new, ArrayList::addAll);
		map.values().forEach(e -> e.sort(null));
		ArtifactSetConfig ans = new ArtifactSetConfig();
		map.forEach((k, v) -> v.forEach(e -> e.validate(k)));
		cache = ans;
		return ans;
	}

	@SerialClass.SerialField
	public HashMap<ArtifactSet, ArrayList<Entry>> map = new HashMap<>();

	@SerialClass
	public static class Entry implements Comparable<Entry> {

		@SerialClass.SerialField
		public int count;

		@SerialClass.SerialField
		public SetEffect effect;

		public String str;
		public UUID[] id;

		@Override
		public int compareTo(@NotNull ArtifactSetConfig.Entry o) {
			int ans = Integer.compare(count, o.count);
			if (ans != 0) return ans;
			return effect.getID().compareTo(o.effect.getID());
		}

		public void validate(ArtifactSet set) {
			str = set.getID() + "_" + effect.getID();
			id = new UUID[effect.ids];
			for (int i = 0; i < effect.ids; i++)
				id[i] = MathHelper.getUUIDfromString(str + "_" + i);
		}

	}

}
