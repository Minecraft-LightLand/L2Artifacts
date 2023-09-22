package dev.xkmc.l2artifacts.init.registrate.entries;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.OneTimeEventReceiver;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.datapack.DatapackInstance;
import dev.xkmc.l2artifacts.datapack.EntryHolder;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistryEvent;

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
		return (LinearFuncEntry) this.entry(id, cb -> new LinearFuncBuilder<>(this, this, id, cb, set, LinearFuncHandle::new, base, slope)).register();
	}

	<T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return this.entry(id, cb -> new SetEffectBuilder<>(this, this, id, cb, sup));
	}

	public SetRegHelper getSetHelper(String id) {
		return new SetRegHelper(this, id);
	}

	//TODO removal
	public <T, R extends EntryHolder<T, R>> DatapackInstance<T, R> datapackRegistry(String id, Class<R> cls, DatapackInstance.Factory<T, R> func, Codec<T> codec) {
		DatapackInstance<T, R> ans = new DatapackInstance<>(new ResourceLocation(getModid(), id), cls, func);
		OneTimeEventReceiver.addModListener(this, DataPackRegistryEvent.NewRegistry.class, e -> e.dataPackRegistry(ans.key, codec, codec));
		return ans;
	}

}
