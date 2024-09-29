package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class TimedCAData extends AttributeSetData {

	@SerialField
	public int time;

}
