package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record SetGroup(ArrayList<ArtifactSet> set) {

	public static SetGroup of(int rank, Collection<SetEntry<?>> sets) {
		return new SetGroup(new ArrayList<>(sets.stream().filter(e -> e.hasRank(rank)).map(DeferredHolder::get).toList()));
	}

	public static SetGroup ofGroup(int rank, Collection<SetEntry<?>> sets) {
		return new SetGroup(new ArrayList<>(sets.stream().filter(e -> e.items.length == 5 && e.hasRank(rank)).map(DeferredHolder::get).toList()));
	}

	public List<SetEntry<?>> getSets(int rank, boolean full) {
		ArrayList<SetEntry<?>> ans = new ArrayList<>();
		for (var e : set) {
			var x = L2Artifacts.REGISTRATE.SET_MAP.get(e.getRegistryName());
			if (x == null || !x.hasRank(rank)) continue;
			if (full && x.items.length != 5) continue;
			ans.add(x);
		}
		return ans;
	}

}
