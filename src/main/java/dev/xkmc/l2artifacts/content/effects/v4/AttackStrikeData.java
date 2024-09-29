package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class AttackStrikeData extends AttributeSetData {

	@SerialField
	public int count;

}
