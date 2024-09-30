package dev.xkmc.l2artifacts.content.config;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public record StatType(
		double base, double base_low, double base_high,
		double main_low, double main_high, double sub_low,
		double sub_high, Attribute attr,
		AttributeModifier.Operation op,
		@Nullable ResourceLocation icon
) {

	public static final ResourceLocation DUMMY_ID = L2Artifacts.loc("dummy");

	@Nullable
	public static StatTypeHolder get(RegistryAccess access, ResourceLocation id) {
		var holder = ArtifactTypeRegistry.STAT_TYPE.get(access, id);
		return holder == null ? null : new StatTypeHolder(holder);
	}

	public static Collection<StatTypeHolder> getValues(RegistryAccess access) {
		return ArtifactTypeRegistry.STAT_TYPE.getAll(access).map(StatTypeHolder::new).toList();
	}

	public double getInitialValue(RandomSource random, boolean max) {
		return max ? base_high : Mth.nextDouble(random, base_low, base_high);
	}

	public double getMainValue(RandomSource random, boolean max) {
		return max ? main_high : Mth.nextDouble(random, main_low, main_high);
	}

	public double getSubValue(RandomSource random, boolean max) {
		return max ? base_high : Mth.nextDouble(random, sub_low, sub_high);
	}

	public MutableComponent getValueText(double val) {
		return attr.toValueComponent(op, val, TooltipFlag.NORMAL);
	}

	public Component getTooltip(double val) {
		return attr.toComponent(new AttributeModifier(DUMMY_ID, val, op), TooltipFlag.NORMAL);
	}

	public double getBaseValue() {
		return base;
	}

	public MutableComponent getDesc() {
		return Component.translatable(attr.getDescriptionId());
	}

}
