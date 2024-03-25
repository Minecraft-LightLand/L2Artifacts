package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class PeriodicData extends SetEffectData {

	@SerialClass.SerialField
	public int tick_count;

}
