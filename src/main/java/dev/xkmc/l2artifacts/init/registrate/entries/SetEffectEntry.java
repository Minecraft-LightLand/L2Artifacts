package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEffectEntry<T extends SetEffect> extends RegistryEntry<SetEffect, T> {

	public SetEffectEntry(AbstractRegistrate<?> owner, DeferredHolder<SetEffect, T> key) {
		super(owner, key);
	}

}
