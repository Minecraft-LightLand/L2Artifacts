package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2serial.serialization.custom_handler.StringClassHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = L2Artifacts.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArtifactDatapackRegistry {

	public static final DatapackInstance<ArtifactStatType, ArtifactStatTypeHolder> STAT_TYPE =
			new DatapackInstance<>("stat_type", ArtifactStatTypeHolder.class, ArtifactStatTypeHolder::new);

	@SubscribeEvent
	public static void registerNewDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(STAT_TYPE.key, ArtifactStatType.CODEC, ArtifactStatType.CODEC);
	}

	public static class DatapackInstance<T, R extends EntryHolder<T>> {

		public final ResourceKey<Registry<T>> key;
		public final Function<Holder.Reference<T>, R> func;

		public DatapackInstance(String id, Class<R> cls, Function<Holder.Reference<T>, R> func) {
			key = ResourceKey.createRegistryKey(new ResourceLocation(L2Artifacts.MODID, id));
			this.func = func;
			new StringClassHandler<R>(cls,
					str -> getValue(Proxy.getWorld(), new ResourceLocation(str)).get(),
					e -> e.getID().toString());
		}

		public ResourceKey<T> entryKey(ResourceLocation key) {
			return ResourceKey.create(this.key, key);
		}

		public Collection<R> getValues(Level level) {
			return level.registryAccess().registryOrThrow(key).holders().map(func).toList();
		}

		public Optional<R> getValue(Level level, ResourceLocation id) {
			return level.registryAccess().registryOrThrow(key).getHolder(entryKey(id)).map(func);
		}
	}

	public interface EntryHolder<T> {

		ResourceLocation getID();

	}

}
