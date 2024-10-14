package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public record StatEntry(Holder<StatType> type, double value) {

	public ResourceLocation getID() {
		return type.unwrapKey().orElseThrow().location();
	}

	public Component getTooltip(@Nullable TooltipFlag flag) {
		return type.value().getTooltip(value(), flag);
	}

	@Override
	public double value() {
		return value * type().value().getBaseValue();
	}

	public void toModifier(ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder, ResourceLocation base) {
		var e = type().value();
		var id = base.withSuffix("_" + getID().getPath());
		builder.put(e.attr(), new AttributeModifier(id, value(), e.op()));
	}

	public Mutable mutable() {
		return new Mutable(type, value);
	}

	public static class Mutable {

		private final Holder<StatType> type;
		private double value;

		private Mutable(Holder<StatType> type, double value) {
			this.type = type;
			this.value = value;
		}


		public void addMultiplier(double val) {
			value += val;
		}

		public StatEntry immutable() {
			return new StatEntry(type, value);
		}

		public Holder<StatType> type() {
			return type;
		}
	}

}
