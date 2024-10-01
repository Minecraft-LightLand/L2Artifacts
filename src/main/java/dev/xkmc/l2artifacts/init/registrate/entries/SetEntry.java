package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<ArtifactSet, T> {

	public final ItemEntry<BaseArtifact>[][] items;

	public SetEntry(ArtifactRegistrate owner, DeferredHolder<ArtifactSet, T> delegate,
					ItemEntry<BaseArtifact>[][] items) {
		super(owner, delegate);
		this.items = items;
		owner.SET_LIST.add(this);
		owner.SET_MAP.put(getId(), this);
	}

	public boolean hasRank(int rank) {
		return items[0][0].get().rank <= rank && rank <= items[0][items[0].length - 1].get().rank;
	}

	public ItemStack getItem(int slot, int rank) {
		return items[slot][rank - items[slot][0].get().rank].asStack();
	}

}
