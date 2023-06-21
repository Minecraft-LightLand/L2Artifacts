package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<T> {

	public final ItemEntry<BaseArtifact>[][] items;
	public final Consumer<ArtifactSetConfig.SetBuilder> builder;

	public SetEntry(ArtifactRegistrate owner, RegistryObject<T> delegate,
					ItemEntry<BaseArtifact>[][] items,
					Consumer<ArtifactSetConfig.SetBuilder> builder) {
		super(owner, delegate);
		this.items = items;
		this.builder = builder;
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
