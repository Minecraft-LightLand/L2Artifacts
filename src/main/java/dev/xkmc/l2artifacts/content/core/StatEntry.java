package dev.xkmc.l2artifacts.content.core;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.math.MathHelper;
import net.minecraft.network.chat.Component;

import java.util.UUID;

@SerialClass
public class StatEntry {

	@SerialClass.SerialField
	public ArtifactStatType type;

	@SerialClass.SerialField
	public double value;

	private String name;

	public UUID id;

	@Deprecated
	public StatEntry() {

	}

	public StatEntry(ArtifactSlot slot, ArtifactStatType type, double value) {
		this.type = type;
		this.value = value;
		init(slot);
	}

	protected void init(ArtifactSlot slot) {
		name = RegistrateLangProvider.toEnglishName(slot.getRegistryName().getPath());
		String str = slot.getID() + "-" + type.getID();
		this.id = MathHelper.getUUIDFromString(str);
	}

	public Component getTooltip() {
		return type.getTooltip(value);
	}

	public String getName() {
		return name;
	}

}
