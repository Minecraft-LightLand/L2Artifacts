package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public record Upgrade(int main, int sub, ArrayList<Holder<StatType>> stats) {

	public static final Upgrade EMPTY = new Upgrade(0, 0, new ArrayList<>());

	public Upgrade add(Type type) {
		if (type == Type.BOOST_MAIN_STAT) return new Upgrade(main + 1, sub, stats);
		if (type == Type.BOOST_SUB_STAT) return new Upgrade(main, sub + 1, stats);
		return this;
	}

	public Mutable mutable() {
		return new Mutable(this);
	}

	public enum Type {
		BOOST_MAIN_STAT, BOOST_SUB_STAT, SET_SUB_STAT
	}

	public static class Mutable {

		private int main, sub;
		private final ArrayList<Holder<StatType>> stats;

		private Mutable(Upgrade self) {
			main = self.main;
			sub = self.sub;
			stats = new ArrayList<>(self.stats);
		}

		public boolean removeMain() {
			if (main <= 0) return false;
			main--;
			return true;
		}

		public boolean removeSub() {
			if (sub <= 0) return false;
			sub--;
			return true;
		}

		public @Nullable Holder<StatType> removeStat() {
			if (stats.isEmpty()) return null;
			return stats.removeFirst();
		}


		public @Nullable StatEntry selectAmong(ArrayList<StatEntry> set) {
			if (stats.isEmpty()) return null;
			var first = stats.getFirst();
			for (var e : set) {
				if (e.getType().equals(first)) {
					stats.removeFirst();
					return e;
				}
			}
			return null;
		}

		public Upgrade immutable() {
			return new Upgrade(main, sub, stats);
		}

	}

}
