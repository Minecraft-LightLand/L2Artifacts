package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2serial.serialization.SerialClass;

@SerialClass
public class TimedCAData extends AttributeSetData {

	@SerialClass.SerialField
	public int time;

}
