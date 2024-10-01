package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2library.util.GenericItemStack;

@FunctionalInterface
public interface IArtifactPredicate<T> {

	boolean test(GenericItemStack<BaseArtifact> item, T t);

}
