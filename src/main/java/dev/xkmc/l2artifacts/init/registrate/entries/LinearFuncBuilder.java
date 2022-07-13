package dev.xkmc.l2artifacts.init.registrate.entries;

import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LinearFuncBuilder<P> extends AbstractBuilder<LinearFuncHandle, LinearFuncHandle, P, LinearFuncBuilder<P>> {

	private final NonNullSupplier<LinearFuncHandle> sup;

	private final double base, slope;

	public LinearFuncBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<LinearFuncHandle> sup, double base, double slope) {
		super(owner, parent, name, callback, ArtifactTypeRegistry.LINEAR.key());
		this.sup = sup;
		this.base = base;
		this.slope = slope;
	}

	@Override
	protected RegistryEntry<LinearFuncHandle> createEntryWrapper(RegistryObject<LinearFuncHandle> delegate) {
		return new LinearFuncEntry(Wrappers.cast(this.getOwner()), delegate, base, slope);
	}

	@NonnullType
	@NotNull
	protected LinearFuncHandle createEntry() {
		return this.sup.get();
	}

}
