package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.entries.*;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ArtifactRegistrate extends L2Registrate {

	public ArtifactRegistrate() {
		super(L2Artifacts.MODID);
	}

	public final TreeMap<ResourceLocation, SetEntry<?>> SET_MAP = new TreeMap<>();
	public final List<SetEntry<?>> SET_LIST = new ArrayList<>();
	public final List<LinearFuncEntry> LINEAR_LIST = new ArrayList<>();

	public final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ArtifactRegistrate> regSet(String id, NonNullSupplier<T> sup, int min_rank, int max_rank, String name) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, this, id, cb, sup, min_rank, max_rank)).lang(name);
	}

	public final LinearFuncEntry regLinear(String id, double base, double slope) {
		return (LinearFuncEntry) this.entry(id, cb -> new LinearFuncBuilder<>(this, this, id, cb, LinearFuncHandle::new, base, slope)).register();
	}

	public <T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return this.entry(id, cb -> new SetEffectBuilder<>(this, this, id, cb, sup));
	}

}
