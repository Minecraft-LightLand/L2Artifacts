package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import net.minecraftforge.registries.RegistryObject;

public class LinearFuncEntry extends RegistryEntry<LinearFuncHandle> {

	public final double base, slope;

	public LinearFuncEntry(ArtifactRegistrate owner, RegistryObject<LinearFuncHandle> delegate, double base, double slope) {
		super(owner, delegate);
		this.base = base;
		this.slope = slope;
		owner.LINEAR_LIST.add(this);
	}

	public double getFromRank(int rank) {
		return get().getValue(rank - 1);
	}
}
