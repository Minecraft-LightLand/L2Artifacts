
package dev.xkmc.l2artifacts.datapack;

import dev.xkmc.l2library.util.annotation.DataGenOnly;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public final class DatapackEntry<T, R extends EntryHolder<T, R>> {

	private final EntrySetBuilder<T, R> reg;
	private final ResourceKey<T> key;
	private final Supplier<T> sup;

	DatapackEntry(
			EntrySetBuilder<T, R> reg, ResourceKey<T> key, Supplier<T> sup
	) {
		this.reg = reg;
		this.key = key;
		this.sup = sup;
	}

	@DataGenOnly
	public R get(HolderLookup.Provider registries) {
		return reg.registry.func.get(reg.registry, key.location());
	}

	public DatapackInstance<T, R> reg() {
		return reg.registry;
	}

	public ResourceKey<T> key() {
		return key;
	}

	Supplier<T> sup() {
		return sup;
	}


}