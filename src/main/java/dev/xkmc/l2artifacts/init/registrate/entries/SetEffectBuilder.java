package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class SetEffectBuilder<T extends SetEffect, P> extends AbstractBuilder<SetEffect, T, P, SetEffectBuilder<T, P>> {

	private final NonNullSupplier<T> sup;

	public SetEffectBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup) {
		super(owner, parent, name, callback, ArtifactTypeRegistry.SET_EFFECT.key());
		this.sup = sup;
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public SetEffectBuilder<T, P> desc(String name, String def) {
		return this.setData(ProviderType.LANG, (ctx, prov) -> {
			prov.add(ctx.getEntry().getDescriptionId(), name);
			prov.add(ctx.getEntry().getDescriptionId() + ".desc", def);
		});
	}

	public SetEffectBuilder<T, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	@Override
	protected SetEffectEntry<T> createEntryWrapper(DeferredHolder<SetEffect, T> delegate) {
		return new SetEffectEntry<>(Wrappers.cast(this.getOwner()), delegate);
	}

	@Override
	public SetEffectEntry<T> register() {
		return Wrappers.cast(super.register());
	}

}
