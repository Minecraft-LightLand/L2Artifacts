package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.capability.SetEffectData;
import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffect;

public class SunBlockMask extends PersistentDataSetEffect<SetEffectData> {

	public SunBlockMask() {
		super(0);
	}

	@Override
	public SetEffectData getData() {
		return new SetEffectData();
	}
}
