package dev.xkmc.l2artifacts.compat.kubejs.builder;

import dev.latvian.mods.kubejs.BuilderBase;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class ArtifactItemBB extends BuilderBase<Item> {

	private ResourceLocation set, slot;
	private int rank = 1;

	public ArtifactItemBB(ResourceLocation i) {
		super(i);
	}

	public ArtifactItemBB setArtifactSet(String set) {
		this.set = new ResourceLocation(set);
		return this;
	}

	public ArtifactItemBB setArtifactSlot(String slot) {
		this.slot = new ResourceLocation(slot);
		return this;
	}

	public ArtifactItemBB setRank(int rank) {
		this.rank = rank;
		return this;
	}

	@Override
	public RegistryObjectBuilderTypes<? super Item> getRegistryType() {
		return RegistryObjectBuilderTypes.ITEM;
	}

	@Override
	public Item createObject() {
		Objects.requireNonNull(slot);
		Objects.requireNonNull(set);
		return new BaseArtifact(new Item.Properties(),
				() -> ArtifactRegistry.SET.getValue(set),
				() -> ArtifactRegistry.SLOT.getValue(slot),
				rank);
	}

}
