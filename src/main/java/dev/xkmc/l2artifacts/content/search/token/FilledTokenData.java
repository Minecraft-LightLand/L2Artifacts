package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.filter.ArtifactFilterData;
import dev.xkmc.l2library.util.GenericItemStack;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class FilledTokenData {

	protected final List<ItemStack> list;

	@Nullable
	private List<GenericItemStack<BaseArtifact>> cache = null;

	public FilledTokenData(List<ItemStack> list) {
		this.list = list;
	}

	public List<ItemStack> getAll() {
		return list;
	}

	public void clearCache() {
		cache = null;
	}

	public List<GenericItemStack<BaseArtifact>> getFiltered(ArtifactFilterData filter) {
		if (cache != null) return cache;
		cache = filter.getFiltered(this).sorted(filter.getComparator()).toList();
		return cache;
	}

	public int totalSize() {
		return list.size();
	}

	public void add(ItemStack stack) {
		list.add(stack);
	}

}
