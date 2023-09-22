package dev.xkmc.l2artifacts.datapack;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2serial.serialization.custom_handler.StringClassHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Optional;

public class DatapackInstance<T, R extends EntryHolder<T, R>> {

	public final ResourceKey<Registry<T>> key;
	public final Factory<T, R> func;

	public DatapackInstance(ResourceLocation id, Class<R> cls, Factory<T, R> func) {
		key = ResourceKey.createRegistryKey(id);
		this.func = func;
		new StringClassHandler<>(cls,
				str -> func.get(this, new ResourceLocation(str)),
				e -> e.getID().toString());
	}

	public ResourceKey<T> entryKey(ResourceLocation key) {
		return ResourceKey.create(this.key, key);
	}

	public String getEntryDescID(ResourceLocation id) {
		return key.location().getPath() + "." + id.getNamespace() + "." + id.getPath();
	}

	public EntrySetBuilder<T, R> startGen(L2Registrate r) {
		return new EntrySetBuilder<>(r, this);
	}

	public Collection<R> getValues(Level level) {
		return level.registryAccess().registryOrThrow(key).keySet().stream().map(e -> func.get(this, e)).toList();
	}

	public Optional<R> getValue(Level level, ResourceLocation id) {
		return Optional.of(func.get(this, id));
	}

	public interface Factory<T, R extends EntryHolder<T, R>> {

		R get(DatapackInstance<T, R> registry, ResourceLocation id);

	}

}
