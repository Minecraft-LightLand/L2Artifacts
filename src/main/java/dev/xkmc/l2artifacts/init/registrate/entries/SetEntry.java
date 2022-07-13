package dev.xkmc.l2artifacts.init.registrate.entries;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2library.repack.registrate.AbstractRegistrate;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class SetEntry<T extends ArtifactSet> extends RegistryEntry<T> {

	public final ItemEntry<BaseArtifact>[][] items;

	public SetEntry(ArtifactRegistrate owner, RegistryObject<T> delegate, ItemEntry<BaseArtifact>[][] items) {
		super(owner, delegate);
		this.items = items;
		owner.SET_LIST.add(this);
	}

}
