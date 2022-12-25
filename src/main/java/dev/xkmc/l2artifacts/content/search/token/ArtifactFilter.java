package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@SerialClass
public class ArtifactFilter<T extends IArtifactFeature> implements IArtifactFilter {

	public final List<T> allEntries;
	private final IArtifactFilter parent;
	private final IArtifactPredicate<T> func;
	private final LangData desc;

	@SerialClass.SerialField
	private final boolean[] selected;

	@Nullable
	private boolean[] availability;

	public ArtifactFilter(IArtifactFilter parent, LangData desc, Collection<T> reg, IArtifactPredicate<T> func) {
		this.parent = parent;
		allEntries = new ArrayList<>(reg);
		this.func = func;
		this.desc = desc;
		selected = new boolean[allEntries.size()];
		for (int i = 0; i < allEntries.size(); i++) {
			selected[i] = true;
		}
	}

	public void toggle(int i) {
		selected[i] ^= true;
		update();
	}

	public boolean getSelected(int i) {
		return selected[i];
	}

	public boolean[] getAvailability() {
		if (availability != null) return availability;
		availability = new boolean[allEntries.size()];
		List<GenericItemStack<BaseArtifact>> list = parent.getAvailableArtifacts().toList();
		for (int i = 0; i < allEntries.size(); i++) {
			int I = i;
			availability[i] = list.stream().anyMatch(e -> func.test(e, allEntries.get(I)));
		}
		return availability;
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
}
