package dev.xkmc.l2artifacts.content.effects.v4;


import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class LongShooterPersistentData extends AttributeSetData {

	@SerialField
	public boolean old;//If the monster is scanned, it will be changed to false; if not, it will be true


}
