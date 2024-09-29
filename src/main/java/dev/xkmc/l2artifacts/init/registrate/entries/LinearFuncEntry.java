package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.util.Proxy;
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
		var access = Proxy.getRegistryAccess();
		if (access != null) {
			var c = ArtifactTypeRegistry.LINEAR_CONFIG.get(access, this);
			if (c != null) {
				return c.base() + c.slope() * (rank - 1);
			}
		}
		return base + slope * (rank - 1);
	}
}
