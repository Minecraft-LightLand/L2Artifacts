package dev.xkmc.l2artifacts.content.core;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.network.chat.Component;

import java.util.UUID;

@SerialClass
public class StatEntry {

	@SerialClass.SerialField
	public ArtifactStatTypeHolder type;

	@SerialClass.SerialField
	private double value;

	private String name;

	public UUID id;

	@Deprecated
	public StatEntry() {

	}

	public StatEntry(ArtifactSlot slot, ArtifactStatTypeHolder type, double value) {
		this.type = type;
		this.value = value;
		init(slot);
	}

	protected void init(ArtifactSlot slot) {
		name = RegistrateLangProvider.toEnglishName(slot.getRegistryName().getPath());
		String str = slot.getID() + "-" + type;
		this.id = MathHelper.getUUIDFromString(str);
	}

	public Component getTooltip() {
		return type.get().getTooltip(getValue());
	}

	public double getValue() {
		return value * type.get().getBaseValue();
	}

	public void addMultiplier(double value) {
		this.value += value;
	}

	public String getName() {
		return name;
	}

}
