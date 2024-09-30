package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public record ArtifactSetConfig(ArrayList<Entry> entries) {

	public static ArtifactSetConfig construct(Consumer<SetBuilder> builder) {
		ArrayList<Entry> list = new ArrayList<>();
		builder.accept((count, effect) -> list.add(new Entry(count, effect)));
		return new ArtifactSetConfig(list);
	}

	public record Entry(int count, SetEffect effect) implements Comparable<Entry> {

		@Override
		public int compareTo(@NotNull ArtifactSetConfig.Entry o) {
			int ans = Integer.compare(count, o.count);
			if (ans != 0) return ans;
			return effect.getID().compareTo(o.effect.getID());
		}

	}

	public interface SetBuilder {

		void addImpl(int count, SetEffect effect);

		default SetBuilder add(int count, SetEffect effect) {
			addImpl(count, effect);
			return this;
		}

	}

}
