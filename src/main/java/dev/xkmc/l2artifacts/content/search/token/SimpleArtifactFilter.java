package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

@SerialClass
public class SimpleArtifactFilter<T extends IArtifactFeature> extends ArtifactFilter<T> {

	private static <T> Collection<T> wrap(Iterable<T> in) {
		ArrayList<T> ans = new ArrayList<>();
		for (T t : in) {
			ans.add(t);
		}
		return ans;
	}

	private final IArtifactExtractor<T> func;

	public SimpleArtifactFilter(IArtifactFilter parent, LangData desc, Iterable<T> reg, IArtifactExtractor<T> func) {
		super(parent, desc, wrap(reg), (item, t) -> func.get(item.item()) == t);
		this.func = func;
	}

	@Override
	public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
		return Comparator.comparingInt(e -> item_priority[revMap.get(func.get(e.item()))]);
	}
}
