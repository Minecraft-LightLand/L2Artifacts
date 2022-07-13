package dev.xkmc.l2artifacts.init.registrate.entries;

import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
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

}
