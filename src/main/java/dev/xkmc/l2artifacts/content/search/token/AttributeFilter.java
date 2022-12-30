package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.code.GenericItemStack;
import it.unimi.dsi.fastutil.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@SerialClass
public class AttributeFilter extends ArtifactFilter<ArtifactStatType> {

	public AttributeFilter(IArtifactFilter parent, LangData desc, Collection<ArtifactStatType> reg) {
		super(parent, desc, reg, (item, type) -> BaseArtifact.getStats(item.stack()).map(x -> x.map.containsKey(type)).orElse(false));
	}

	@Override
	public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
		Comparator<GenericItemStack<BaseArtifact>> ans = Comparator.comparingInt(e -> BaseArtifact.getStats(e.stack())
				.map(x -> -item_priority[revMap.get(x.main_stat.type)]).orElse(item_priority.length));
		List<Pair<ArtifactStatType, Integer>> list = new ArrayList<>(allEntries.stream()
				.map(e -> Pair.of(e, revMap.get(e)))
				.filter(e -> getSelected(e.second())).toList());
		list.sort(Comparator.comparingInt(e -> item_priority[e.second()]));
		for (var p : list) {
			ans = ans.thenComparingDouble(e -> BaseArtifact.getStats(e.stack())
					.map(x -> x.map.get(p.left())).map(s -> -s.value).orElse(0d));
		}
		return ans;
	}
}
