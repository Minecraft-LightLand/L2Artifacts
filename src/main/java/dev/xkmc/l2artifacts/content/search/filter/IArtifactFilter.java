package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.token.FilledTokenData;
import dev.xkmc.l2library.util.GenericItemStack;

import java.util.Comparator;
import java.util.stream.Stream;

public interface IArtifactFilter {

	Stream<GenericItemStack<BaseArtifact>> getFilteredImpl(FilledTokenData data);

	void update(FilledTokenData data);

	Comparator<GenericItemStack<BaseArtifact>> getComparator();

}
