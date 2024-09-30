package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@SerialClass
public class StatEntry {

	@SerialField
	public Holder<StatType> type;

	@SerialField
	private double value;

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

	public ResourceLocation getID() {
		return type.unwrapKey().orElseThrow().location();
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

	public void toModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, ResourceLocation base) {
		var e = getType().value();
		var id = base.withSuffix("_" + getID().getPath());
		builder.put(e.attr(), new AttributeModifier(id, getValue(), e.op()));
	}

}
