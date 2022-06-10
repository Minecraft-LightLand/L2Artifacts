package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.base.NamedEntry;

import java.util.Random;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> {

	public ArtifactSlot() {
		super(() -> ArtifactRegistry.SLOT);
	}

	public void generate(ArtifactStats stat, Random random) {

	}

}
