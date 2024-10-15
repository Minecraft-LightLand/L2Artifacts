package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.token.FilledTokenData;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.l2serial.serialization.marker.OnInject;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.ChatFormatting;
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
	private final ArtifactLang desc;

	@SerialField
	private long selection;

	@SerialField
	protected int[] item_priority;

	@SerialField
	protected int sort_priority;

	@Nullable
	private boolean[] availability;

	public ArtifactFilter(IArtifactFilter parent, ArtifactLang desc, Collection<T> reg, IArtifactPredicate<T> func) {
		this.parent = parent;
		this.sort_priority = parent instanceof ArtifactFilter<?> a ? a.sort_priority + 1 : 1;
		allEntries = new ArrayList<>(reg);
		this.func = func;
		this.desc = desc;
		selection = 0;
		item_priority = new int[allEntries.size()];
		for (int i = 0; i < allEntries.size(); i++) {
			toggle(i);
			item_priority[i] = i + 1;
			revMap.put(allEntries.get(i), i);
		}
	}

	private boolean isSelected(int i) {
		return (selection ^ (1L << i)) != 0;
	}

	private void toggle(int i) {
		selection ^= 1L << i;
	}

	public void toggle(FilledTokenData data, int ind) {
		toggle(ind);
		if (isSelected(ind)) {
			prioritize(ind);
		} else {
			item_priority[ind] = 0;
		}
		update(data);
	}

	public void prioritize(int ind) {
		item_priority[ind] = 0;
		List<T> list = new ArrayList<>(allEntries.stream().filter(e -> isSelected(revMap.get(e))).toList());
		list.sort(Comparator.comparingInt(e -> item_priority[revMap.get(e)]));
		for (int i = 0; i < allEntries.size(); i++) {
			item_priority[i] = 0;
		}
		for (int i = 0; i < list.size(); i++) {
			item_priority[revMap.get(list.get(i))] = i + 1;
		}
	}

	public boolean getSelected(int i) {
		return isSelected(i);
	}

	public boolean getAvailability(FilledTokenData data, int j) {
		if (availability != null) return availability[j];
		availability = new boolean[allEntries.size()];
		List<GenericItemStack<BaseArtifact>> list = parent.getFilteredImpl(data).toList();
		for (int i = 0; i < allEntries.size(); i++) {
			int I = i;
			availability[i] = list.stream().anyMatch(e -> func.test(e, allEntries.get(I)));
		}
		return availability[j];
	}

	private boolean isValid(GenericItemStack<BaseArtifact> item) {
		for (int i = 0; i < allEntries.size(); i++) {
			if (isSelected(i) && func.test(item, allEntries.get(i))) {
				return true;
			}
		}
		return false;
	}

	@Deprecated
	public void update(FilledTokenData data) {
		parent.update(data);
	}

	protected void clearCache() {
		availability = null;
	}

	@Override
	public Stream<GenericItemStack<BaseArtifact>> getFilteredImpl(FilledTokenData data) {
		return parent.getFilteredImpl(data).filter(this::isValid);
	}

	public Component getDescription() {
		return desc.get().withStyle(ChatFormatting.GRAY);
	}

	public int getPriority(int j) {
		return item_priority[j];
	}

	public int priority() {
		return sort_priority;
	}

	@OnInject
	public void postInject() {
		int size = allEntries.size();
		if (item_priority.length < size) {
			item_priority = Arrays.copyOf(item_priority, size);
		}
	}

	@Override
	public void initFilter() {
		int enabled = 0;
		for (var i = 0; i < item_priority.length; i++) {
			if (isSelected(i)) enabled++;
		}
		for (int i = 0; i < item_priority.length; i++) {
			if (!isSelected(i)) {
				toggle(i);
				item_priority[i] = ++enabled;
			}
		}
		clearCache();
	}

}
