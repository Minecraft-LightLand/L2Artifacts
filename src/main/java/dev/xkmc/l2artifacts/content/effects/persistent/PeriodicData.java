package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.effects.SetEffectData;
import dev.xkmc.l2library.serial.SerialClass;

@SerialClass
public class PeriodicData extends SetEffectData {

	@SerialClass.SerialField
	public int tick_count;

}
