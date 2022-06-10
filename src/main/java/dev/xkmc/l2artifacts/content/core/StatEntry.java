package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.MathHelper;

import java.util.UUID;

@SerialClass
public class StatEntry {

	@SerialClass.SerialField
	public ArtifactStatType type;

	@SerialClass.SerialField
	public double value;

	public String name;

	public UUID id;

	@Deprecated
	public StatEntry() {

	}

	public StatEntry(ArtifactSlot slot, ArtifactStatType type, double value) {
		this.type = type;
		this.value = value;
		init(slot);
	}

	protected void init(ArtifactSlot slot){
		this.name = slot.getID() + "-" + type.getID();
		this.id = MathHelper.getUUIDfromString(name);
	}


}
