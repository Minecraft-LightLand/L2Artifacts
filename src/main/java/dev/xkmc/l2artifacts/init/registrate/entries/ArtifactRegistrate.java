package dev.xkmc.l2artifacts.init.registrate.entries;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.config.LinearParam;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.LinearFunc;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
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
	public final Multimap<ResourceLocation, LinearFuncEntry> LINEAR_LIST = LinkedListMultimap.create();

	final <T extends ArtifactSet> SetBuilder<T, BaseArtifact, ArtifactRegistrate> regSet(String id, NonNullSupplier<T> sup, int min_rank, int max_rank, String name) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, this, id, cb, sup, min_rank, max_rank)).lang(name);
	}

	final LinearFuncEntry regLinear(String id, SetRegHelper set, double base, double slope) {
		return (LinearFuncEntry) this.entry(id, cb -> new LinearFuncBuilder<>(this, this, id, cb, set, LinearFunc::new, base, slope))
				.dataMap(ArtifactTypeRegistry.LINEAR_CONFIG.reg(), new LinearParam(base, slope)).register();
	}

	<T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return this.entry(id, cb -> new SetEffectBuilder<>(this, this, id, cb, sup));
	}

	public SetRegHelper getSetHelper(String id) {
		return new SetRegHelper(this, id);
	}

}
