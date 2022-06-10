package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.LcyRegistrate;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.AbstractRegistrate;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ArtifactRegistrate extends LcyRegistrate {

	public ArtifactRegistrate() {
		super(L2Artifacts.MODID);
	}


	public <T extends ArtifactSet> RegistryEntry<T> regSet(String id, NonNullSupplier<T> sup, int min_rank, int max_rank, RegistryEntry<ArtifactSlot>... slots) {
		return this.entry(id, (cb) -> new SetBuilder<>(this, id, cb, sup, min_rank, max_rank, slots))
				.regItems().defaultLang().register();
	}

	public static class SetBuilder<T extends ArtifactSet> extends AbstractBuilder<ArtifactSet, T, ArtifactRegistrate, ArtifactRegistrate.SetBuilder<T>> {

		private final NonNullSupplier<T> sup;
		private final int min_rank, max_rank;
		private final RegistryEntry<ArtifactSlot>[] slots;

		@SafeVarargs
		SetBuilder(ArtifactRegistrate parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, int min_rank, int max_rank, RegistryEntry<ArtifactSlot>... slots) {
			super(parent, parent, name, callback, ArtifactSet.class);
			this.sup = sup;
			this.min_rank = min_rank;
			this.max_rank = max_rank;
			this.slots = slots;
		}

		public SetBuilder<T> regItems() {
			for (int i = min_rank; i <= max_rank; i++) {
				for (RegistryEntry<ArtifactSlot> slot : slots) {
					String name = this.getName() + "_" + slot.getId().getPath() + "_" + i;
					int rank = i;
					L2Artifacts.REGISTRATE.item(name, p -> new BaseArtifact(p, slot, rank)).build();
				}
			}
			return this;
		}

		@NonnullType
		@NotNull
		protected T createEntry() {
			return this.sup.get();
		}

		public SetBuilder<T> defaultLang() {
			return this.lang(NamedEntry::getDescriptionId);
		}
	}

	public static class SetEntry<T extends ArtifactSet> extends RegistryEntry<T> {

		public SetEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
			super(owner, delegate);
		}

	}

}
