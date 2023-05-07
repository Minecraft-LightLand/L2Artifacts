package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

@SerialClass
public abstract class ArtifactFilter<T extends IArtifactFeature> implements IArtifactFilter {

	public final List<T> allEntries;

	protected final Map<T, Integer> revMap = new HashMap<>();

	private final IArtifactFilter parent;
	private final IArtifactPredicate<T> func;
	private final LangData desc;

	@SerialClass.SerialField
	private final boolean[] selected;

	@SerialClass.SerialField
	protected final int[] item_priority;

	@SerialClass.SerialField
	protected int sort_priority;

	@Nullable
	private boolean[] availability;

	public ArtifactFilter(IArtifactFilter parent, LangData desc, Collection<T> reg, IArtifactPredicate<T> func) {
		this.parent = parent;
		this.sort_priority = parent instanceof ArtifactFilter a ? a.sort_priority + 1 : 1;
		allEntries = new ArrayList<>(reg);
		this.func = func;
		this.desc = desc;
		selected = new boolean[allEntries.size()];
		item_priority = new int[allEntries.size()];
		for (int i = 0; i < allEntries.size(); i++) {
			selected[i] = true;
			item_priority[i] = i + 1;
			revMap.put(allEntries.get(i), i);
		}
	}

	public void toggle(int ind) {
		selected[ind] ^= true;
		if (selected[ind]) {
			prioritize(ind);
		} else {
			item_priority[ind] = 0;
		}
		update();
	}

	public void prioritize(int ind) {
		item_priority[ind] = 0;
		List<T> list = new ArrayList<>(allEntries.stream().filter(e -> selected[revMap.get(e)]).toList());
		list.sort(Comparator.comparingInt(e -> item_priority[revMap.get(e)]));
		for (int i = 0; i < allEntries.size(); i++) {
			item_priority[i] = 0;
		}
		for (int i = 0; i < list.size(); i++) {
			item_priority[revMap.get(list.get(i))] = i + 1;
		}
	}

	public boolean getSelected(int i) {
		return selected[i];
	}

	public boolean getAvailability(int j) {
		if (availability != null) return availability[j];
		availability = new boolean[allEntries.size()];
		List<GenericItemStack<BaseArtifact>> list = parent.getAvailableArtifacts().toList();
		for (int i = 0; i < allEntries.size(); i++) {
			int I = i;
			availability[i] = list.stream().anyMatch(e -> func.test(e, allEntries.get(I)));
		}
		return availability[j];
	}

	private boolean isValid(GenericItemStack<BaseArtifact> item) {
		for (int i = 0; i < allEntries.size(); i++) {
			if (selected[i] && func.test(item, allEntries.get(i))) {
				return true;
			}
		}
		return false;
	}

	@Deprecated
	public void update() {
		parent.update();
	}

	protected void clearCache() {
		availability = null;
	}

	@Override
	public Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts() {
		return parent.getAvailableArtifacts().filter(this::isValid);
	}

	public Component getDescription() {
		return desc.get();
	}

	public int getPriority(int j) {
		return item_priority[j];
	}

	public int priority() {
		return sort_priority;
	}
}
