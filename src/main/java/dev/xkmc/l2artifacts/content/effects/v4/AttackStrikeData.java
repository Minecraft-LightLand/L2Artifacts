package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2library.serial.SerialClass;

@SerialClass
public class AttackStrikeData extends AttributeSetData {

	@SerialClass.SerialField
	public int count;

}
