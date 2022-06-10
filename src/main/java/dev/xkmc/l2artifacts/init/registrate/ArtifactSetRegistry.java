package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class ArtifactSetRegistry {

	private static ArtifactSet regSet(String id, ArtifactSet set) {
		REGISTRATE.generic(ArtifactSet.class, id, () -> set).defaultLang().build();
		return set;
	}

}
