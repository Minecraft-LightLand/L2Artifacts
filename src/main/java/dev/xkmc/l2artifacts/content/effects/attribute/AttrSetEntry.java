package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;

public record AttrSetEntry(Holder<Attribute> attr, AttributeModifier.Operation op,
						   LinearFuncEntry func, boolean usePercent) {

	public double getValue(int rank) {
		return func().getFromRank(rank);
	}

	public ResourceLocation getId(SetEffect set) {
		return set.getRegistryName().withSuffix("_" + attr().unwrapKey().orElseThrow().location().getPath());
	}

	public MutableComponent toComponent(int rank) {
		return attr().value().toComponent(new AttributeModifier(StatType.DUMMY_ID, getValue(rank), op()), TooltipFlag.NORMAL);
	}

}
