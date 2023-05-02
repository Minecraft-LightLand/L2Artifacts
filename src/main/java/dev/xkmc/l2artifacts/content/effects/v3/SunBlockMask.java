package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffectData;

public class SunBlockMask extends PersistentDataSetEffect<SetEffectData> {

	public SunBlockMask() {
		super(0);
	}

	@Override
	public SetEffectData getData(ArtifactSetConfig.Entry ent) {
		return new SetEffectData();
	}
}
