package dev.xkmc.l2artifacts.init.registrate.entries;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.builders.AbstractBuilder;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonnullType;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetBuilder<T extends ArtifactSet, I extends BaseArtifact, P> extends AbstractBuilder<ArtifactSet, T, P, SetBuilder<T, I, P>> {

	private final NonNullSupplier<T> sup;
	private final int min_rank, max_rank;
	private final RegistryEntry<ArtifactSlot>[] slots;

	private ItemEntry<BaseArtifact>[][] items;

	@SafeVarargs
	public SetBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, int min_rank, int max_rank, RegistryEntry<ArtifactSlot>... slots) {
		super(owner, parent, name, callback, ArtifactTypeRegistry.SET.key());
		this.sup = sup;
		this.min_rank = min_rank;
		this.max_rank = max_rank;
		this.slots = slots;
	}

	@SuppressWarnings({"rawtype", "unchecked"})
	public SetBuilder<T, I, P> regItems() {
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
						.model((ctx, pvd) -> pvd.getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/" + getName() + "_" + slot_name))
								.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/sets_ranks_" + rank)))
						.tag(curios_tag, slot_tag, rank_tag).register();
			}
		}
		return this;
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
		return new SetEntry<>(Wrappers.cast(this.getOwner()), delegate, items);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public SetBuilder<T, I, P> defaultLang() {
		return this.lang(NamedEntry::getDescriptionId, RegistrateLangProvider.toEnglishName(this.getName()));
	}
}
