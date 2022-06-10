package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.LcyRegistrate;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class ArtifactRegistrate extends LcyRegistrate {

	public ArtifactRegistrate() {
		super(L2Artifacts.MODID);
	}

}
