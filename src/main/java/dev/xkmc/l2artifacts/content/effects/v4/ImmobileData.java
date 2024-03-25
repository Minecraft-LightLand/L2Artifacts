package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class ImmobileData extends SetEffectData {

	@SerialClass.SerialField
	public double x, y, z;

	@SerialClass.SerialField
	public int time;

}
