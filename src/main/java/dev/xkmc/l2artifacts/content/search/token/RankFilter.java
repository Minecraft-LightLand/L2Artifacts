package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

import java.util.Comparator;

@SerialClass
public class RankFilter extends ArtifactFilter<RankToken> {

	public RankFilter(IArtifactFilter parent, ArtifactLang desc) {
		super(parent, desc, RankToken.ALL_RANKS, (item, t) -> item.item().rank == t.rank());
	}

	@Override
	public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
		return Comparator.comparingInt(e -> -e.item().rank);
	}

	@Override
	public int getPriority(int j) {
		return allEntries.size() - j;
	}

	@Override
	public void prioritize(int ind) {
	}

}
