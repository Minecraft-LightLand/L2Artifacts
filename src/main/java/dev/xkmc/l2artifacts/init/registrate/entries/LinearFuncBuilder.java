package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2artifacts.content.core.LinearFunc;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2serial.util.Wrappers;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class LinearFuncBuilder<P> extends AbstractBuilder<LinearFunc, LinearFunc, P, LinearFuncBuilder<P>> {

	private final NonNullSupplier<LinearFunc> sup;
	private final SetRegHelper set;

	private final double base, slope;

	public LinearFuncBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback,
							 SetRegHelper set,
							 NonNullSupplier<LinearFunc> sup,
							 double base, double slope) {
		super(owner, parent, name, callback, ArtifactTypeRegistry.LINEAR.key());
		this.sup = sup;
		this.base = base;
		this.slope = slope;
		this.set = set;
	}

	@Override
	protected RegistryEntry<LinearFunc, LinearFunc> createEntryWrapper(DeferredHolder<LinearFunc, LinearFunc> delegate) {
		return new LinearFuncEntry(Wrappers.cast(this.getOwner()), set, delegate, base, slope);
	}

	@NonnullType
	@NotNull
	protected LinearFunc createEntry() {
		return this.sup.get();
	}

}
