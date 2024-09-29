package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.Supplier;

public record AttrSetEntry(Holder<Attribute> attr, AttributeModifier.Operation op,
						   LinearFuncEntry func, boolean usePercent) {

	public double getValue(int rank) {
		return func().getFromRank(rank);
	}

}
