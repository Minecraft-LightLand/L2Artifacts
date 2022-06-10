package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.base.NamedEntry;

public class ArtifactSet extends NamedEntry<ArtifactSet> {

	public ArtifactSet(String id) {
		super(() -> ArtifactRegistry.SET);
	}

}
