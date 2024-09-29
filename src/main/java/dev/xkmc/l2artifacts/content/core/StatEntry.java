package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SerialClass
public class StatEntry {

	@SerialField
	public ResourceLocation type;

	@SerialField
	private double value;

	public ResourceLocation id;

	@Deprecated
	public StatEntry() {

	}

	public StatEntry(ArtifactSlot slot, ResourceLocation type, double value) {
		this.type = type;
		this.value = value;
	}

	public StatTypeConfig getType() {
		return StatTypeConfig.get(type);
	}

	public Component getTooltip() {
		return getType().getTooltip(getValue());
	}

	public double getValue() {
		return value * getType().getBaseValue();
	}

	public void addMultiplier(double value) {
		this.value += value;
	}

}
