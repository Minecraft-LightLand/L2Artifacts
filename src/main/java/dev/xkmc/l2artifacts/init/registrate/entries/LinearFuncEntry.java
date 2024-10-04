package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.LinearFunc;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.util.ServerProxy;
import net.neoforged.neoforge.registries.DeferredHolder;

public class LinearFuncEntry extends RegistryEntry<LinearFunc, LinearFunc> {

	public final SetRegHelper set;
	public final double base, slope;

	public LinearFuncEntry(ArtifactRegistrate owner, SetRegHelper set, DeferredHolder<LinearFunc, LinearFunc> delegate, double base, double slope) {
		super(owner, delegate);
		this.base = base;
		this.slope = slope;
		this.set = set;
		owner.LINEAR_LIST.put(set.getId(), this);
	}

	public double getFromRank(int rank) {
		var access = ServerProxy.getRegistryAccess();
		if (access != null) {
			var c = ArtifactTypeRegistry.LINEAR_CONFIG.get(access, this);
			if (c != null) {
				return c.base() + c.slope() * (rank - 1);
			}
		}
		return base + slope * (rank - 1);
	}
}
