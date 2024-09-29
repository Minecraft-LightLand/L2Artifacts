package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LinearFuncEntry extends RegistryEntry<LinearFuncHandle, LinearFuncHandle> {

	public final SetRegHelper set;
	public final double base, slope;

	public LinearFuncEntry(ArtifactRegistrate owner, SetRegHelper set, DeferredHolder<LinearFuncHandle, LinearFuncHandle> delegate, double base, double slope) {
		super(owner, delegate);
		this.base = base;
		this.slope = slope;
		this.set = set;
		owner.LINEAR_LIST.put(set.getId(), this);
	}

	public double getFromRank(int rank) {
		return get().getValue(rank - 1);
	}
}
