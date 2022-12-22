package dev.xkmc.l2artifacts.content.client.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2library.util.code.GenericItemStack;

@FunctionalInterface
public interface IArtifactPredicate<T> {

	boolean test(GenericItemStack<BaseArtifact> item, T t);

}
