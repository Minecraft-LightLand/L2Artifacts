package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class StatEntry {

	@SerialField
	public Holder<StatType> type;

	@SerialField
	private double value;

	public ResourceLocation id;

	@Deprecated
	public StatEntry() {

	}

	public StatEntry(ArtifactSlot slot, Holder<StatType> type, double value) {
		this.type = type;
		this.value = value;
	}

	public Holder<StatType> getType() {
		return type;
	}

	public Component getTooltip() {
		return type.value().getTooltip(getValue());
	}

	public double getValue() {
		return value * getType().value().getBaseValue();
	}

	public void addMultiplier(double value) {
		this.value += value;
	}

}
