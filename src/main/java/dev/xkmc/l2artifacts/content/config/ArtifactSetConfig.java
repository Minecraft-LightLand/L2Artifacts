package dev.xkmc.l2artifacts.content.config;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.capability.conditionals.Context;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2library.serial.config.CollectType;
import dev.xkmc.l2library.serial.config.ConfigCollect;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

@SerialClass
public class ArtifactSetConfig extends BaseConfig {

	public static ArtifactSetConfig getInstance() {
		return NetworkManager.ARTIFACT_SETS.getMerged();
	}

	public static ArtifactSetConfig construct(ArtifactSet set, Consumer<SetBuilder> builder) {
		ArtifactSetConfig config = new ArtifactSetConfig();
		ArrayList<Entry> list = new ArrayList<>();
		builder.accept((count, effect) -> list.add(new Entry(count, effect)));
		config.map.put(set, list);
		return config;
	}

	@ConfigCollect(CollectType.MAP_COLLECT)
	@SerialField
	public HashMap<ArtifactSet, ArrayList<Entry>> map = new HashMap<>();

	@Override
	protected void postMerge() {
		map.values().forEach(e -> e.sort(null));
		map.forEach((k, v) -> v.forEach(e -> e.validate(k)));
	}

	@SerialClass
	public static class Entry implements Comparable<Entry> {

		@SerialField
		public int count;

		@SerialField
		public SetEffect effect;

		private String name;
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
			String str = set.getID() + "_" + effect.getID();
			name = RegistrateLangProvider.toEnglishName(effect.getRegistryName().getPath());
			id = new UUID[effect.ids];
			for (int i = 0; i < effect.ids; i++)
				id[i] = MathHelper.getUUIDFromString(str + "_" + i);
		}

		public String getName() {
			return name;
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
