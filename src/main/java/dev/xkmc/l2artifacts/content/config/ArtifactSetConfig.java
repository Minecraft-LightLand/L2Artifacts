package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.NetworkManager;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

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
		ans.map = map;
		cache = ans;
		return ans;
	}

	public static ArtifactSetConfig construct(ArtifactSet set, Consumer<SetBuilder> builder) {
		ArtifactSetConfig config = new ArtifactSetConfig();
		ArrayList<Entry> list = new ArrayList<>();
		builder.accept((count, effect) -> list.add(new Entry(count, effect)));
		config.map.put(set, list);
		return config;
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

		@Deprecated
		public Entry() {

		}

		Entry(int count, SetEffect effect) {
			this.count = count;
			this.effect = effect;
		}

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
				id[i] = MathHelper.getUUIDFromString(str + "_" + i);
		}

	}

	public interface SetBuilder {

		void add_impl(int count, SetEffect effect);

		default SetBuilder add(int count, SetEffect effect) {
			add_impl(count, effect);
			return this;
		}

	}
}
