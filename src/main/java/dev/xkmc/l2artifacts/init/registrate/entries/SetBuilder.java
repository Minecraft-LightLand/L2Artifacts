package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems.RANK_NAME;

public class SetBuilder<T extends ArtifactSet, I extends BaseArtifact, P> extends AbstractBuilder<ArtifactSet, T, P, SetBuilder<T, I, P>> {

	private final NonNullSupplier<T> sup;
	private final int min_rank, max_rank;

	private SimpleEntry<ArtifactSlot>[] slots;
	private ItemEntry<BaseArtifact>[][] items;
	private Consumer<ArtifactSetConfig.SetBuilder> builder;

	public SetBuilder(ArtifactRegistrate owner, P parent, String name, BuilderCallback callback, NonNullSupplier<T> sup, int min_rank, int max_rank) {
		super(owner, parent, name, callback, ArtifactTypeRegistry.SET.key());
		this.sup = sup;
		this.min_rank = min_rank;
		this.max_rank = max_rank;

	}

	@SafeVarargs
	public final SetBuilder<T, I, P> setSlots(SimpleEntry<ArtifactSlot>... slots) {
		this.slots = slots;
		return this;
	}

	public SetBuilder<T, I, P> buildConfig(Consumer<ArtifactSetConfig.SetBuilder> builder) {
		this.builder = builder;
		return this;
	}

	@SuppressWarnings({"rawtype", "unchecked"})
	public SetBuilder<T, I, P> regItems() {
		if (slots == null) throw new IllegalStateException("call setSlots() first");
		items = new ItemEntry[slots.length][max_rank - min_rank + 1];
		TagKey<Item> artifact = ItemTags.create(L2Artifacts.loc("artifact"));
		for (int i = 0; i < slots.length; i++) {
			SimpleEntry<ArtifactSlot> slot = slots[i];
			String slot_name = slot.key().location().getPath();
			TagKey<Item> curios_tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", "artifact_" + slot_name));
			TagKey<Item> slot_tag = ItemTags.create(L2Artifacts.loc(slot_name));
			for (int r = min_rank; r <= max_rank; r++) {
				TagKey<Item> rank_tag = ItemTags.create(L2Artifacts.loc("rank_" + r));
				String name = this.getName() + "_" + slot_name + "_" + r;
				int rank = r;
				items[i][r - min_rank] = L2Artifacts.REGISTRATE.item(name, p -> new BaseArtifact(p, asSupplier()::get, slot, rank))
						.model((ctx, pvd) -> pvd.getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", L2Artifacts.loc("item/rank/" + rank))
								.texture("layer1", L2Artifacts.loc("item/" + getName() + "/" + slot_name)))
						.tag(curios_tag, slot_tag, rank_tag, artifact).lang(RegistrateLangProvider
								.toEnglishName(this.getName() + "_" + slot_name) + RANK_NAME[r - 1]).register();
			}
		}
		return this;
	}

	@Override
	protected RegistryEntry<ArtifactSet, T> createEntryWrapper(DeferredHolder<ArtifactSet, T> delegate) {
		if (slots == null) throw new IllegalStateException("call setSlots() first");
		if (builder == null) throw new IllegalStateException("call buildConfig() first");
		if (items == null) throw new IllegalStateException("call regItems() first");
		return new SetEntry<>(Wrappers.cast(this.getOwner()), delegate, items, builder);
	}

	@NonnullType
	@NotNull
	protected T createEntry() {
		return this.sup.get();
	}

	public SetBuilder<T, I, P> lang(String name) {
		return this.lang(NamedEntry::getDescriptionId, name);
	}

	@Override
	public SetEntry<T> register() {
		return Wrappers.cast(super.register());
	}
}
