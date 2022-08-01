package dev.xkmc.l2artifacts.init.registrate.entries;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<T> {

	public final ItemEntry<BaseArtifact>[][] items;

	public SetEntry(ArtifactRegistrate owner, RegistryObject<T> delegate, ItemEntry<BaseArtifact>[][] items) {
		super(owner, delegate);
		this.items = items;
		owner.SET_LIST.add(this);
	}

	public boolean hasRank(int rank) {
		return items[0][0].get().rank <= rank && rank <= items[0][items[0].length - 1].get().rank;
	}

	public ItemStack getItem(int slot, int rank) {
		return items[slot][rank - items[slot][0].get().rank].asStack();
	}
}
