package dev.xkmc.l2artifacts.content.client.search.token;

import java.util.List;
import java.util.stream.Stream;

public record RankToken(int rank) implements IArtifactFeature {

	public static final List<RankToken> ALL_RANKS = Stream.of(1, 2, 3, 4, 5).map(RankToken::new).toList();

}
