package dev.xkmc.l2artifacts.datapack;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class EntryHolder<T, E extends EntryHolder<T, E>> {

	private final DatapackInstance<T, E> registry;
	private final ResourceLocation id;

	protected EntryHolder(DatapackInstance<T, E> registry, ResourceLocation id) {
		this.registry = registry;
		this.id = id;
	}

	public ResourceLocation getID() {
		return id;
	}

	public T get(Level level) {
		var ans = level.registryAccess().registryOrThrow(registry.key).get(id);
		assert ans != null;
		return ans;
	}

	public MutableComponent getDesc() {
		return Component.translatable(registry.getEntryDescID(id));
	}

}
