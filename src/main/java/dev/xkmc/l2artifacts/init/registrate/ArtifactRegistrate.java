package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.LcyRegistrate;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.AbstractRegistrate;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ArtifactRegistrate extends LcyRegistrate {

	public ArtifactRegistrate() {
		super(L2Artifacts.MODID);
	}

	@SafeVarargs
	public final <T extends ArtifactSet> SetEntry<T> regSet(String id, NonNullSupplier<T> sup, int min_rank, int max_rank, RegistryEntry<ArtifactSlot>... slots) {
		return (SetEntry<T>) this.entry(id, (cb) -> new SetBuilder<>(this, id, cb, sup, min_rank, max_rank, slots))
				.regItems().defaultLang().register();
	}

	public <T extends SetEffect> RegistryEntry<T> setEffect(String id, NonNullSupplier<T> sup) {
		return generic(SetEffect.class, id, sup).defaultLang().register();

	}

	public static class SetBuilder<T extends ArtifactSet, I extends BaseArtifact> extends AbstractBuilder<ArtifactSet, T, ArtifactRegistrate, ArtifactRegistrate.SetBuilder<T, I>> {

		private final NonNullSupplier<T> sup;
		private final int min_rank, max_rank;
		private final RegistryEntry<ArtifactSlot>[] slots;

		private ItemEntry<BaseArtifact>[][] items;

		@SafeVarargs
		SetBuilder(ArtifactRegistrate parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, int min_rank, int max_rank, RegistryEntry<ArtifactSlot>... slots) {
			super(parent, parent, name, callback, ArtifactSet.class);
			this.sup = sup;
			this.min_rank = min_rank;
			this.max_rank = max_rank;
			this.slots = slots;
		}

		@SuppressWarnings({"rawtype", "unchecked"})
		public SetBuilder<T, I> regItems() {
			items = new ItemEntry[slots.length][max_rank - min_rank + 1];
			ITagManager<Item> manager = Objects.requireNonNull(ForgeRegistries.ITEMS.tags());

			for (int i = 0; i < slots.length; i++) {
				RegistryEntry<ArtifactSlot> slot = slots[i];
				String slot_name = slot.getId().getPath();
				TagKey<Item> curios_tag = manager.createTagKey(new ResourceLocation("curios", slot_name));
				TagKey<Item> slot_tag = manager.createTagKey(new ResourceLocation(L2Artifacts.MODID, slot_name));
				for (int r = min_rank; r <= max_rank; r++) {
					TagKey<Item> rank_tag = manager.createTagKey(new ResourceLocation(L2Artifacts.MODID, "rank_" + r));
					String name = this.getName() + "_" + slot_name + "_" + r;
					int rank = r;
					items[i][r - min_rank] = L2Artifacts.REGISTRATE.item(name, p -> new BaseArtifact(p, asSupplier()::get, slot, rank))
							.tag(curios_tag, slot_tag, rank_tag).register();
				}
			}
			return this;
		}

		@Override
		protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
			return new SetEntry<>(this.getOwner(), delegate, items);
		}

		@NonnullType
		@NotNull
		protected T createEntry() {
			return this.sup.get();
		}

		public SetBuilder<T, I> defaultLang() {
			return this.lang(NamedEntry::getDescriptionId);
		}
	}

	public static class SetEntry<T extends ArtifactSet> extends RegistryEntry<T> {

		public final ItemEntry<BaseArtifact>[][] items;

		public SetEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate, ItemEntry<BaseArtifact>[][] items) {
			super(owner, delegate);
			this.items = items;
		}

	}

}
