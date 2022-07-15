package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.Supplier;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ArtifactStatType extends NamedEntry<ArtifactStatType> {

	private final Supplier<Attribute> attr;
	private final AttributeModifier.Operation op;
	private final boolean usePercent;

	public ArtifactStatType(Supplier<Attribute> attr, AttributeModifier.Operation op, boolean useMult) {
		super(ArtifactTypeRegistry.STAT_TYPE);
		this.attr = attr;
		this.op = op;
		this.usePercent = useMult;
	}

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		builder.put(attr.get(), new AttributeModifier(entry.id, entry.name, entry.value, op));
	}

	public double getInitialValue(int rank, RandomSource random, boolean max) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return (max ? entry.base_high : Mth.nextDouble(random, entry.base_low, entry.base_high)) * rank;
	}

	public double getMainValue(int rank, RandomSource random, boolean max) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return (max ? entry.main_high : Mth.nextDouble(random, entry.main_low, entry.main_high)) * rank;
	}

	public double getSubValue(int rank, RandomSource random, boolean max) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return (max ? entry.sub_high : Mth.nextDouble(random, entry.sub_low, entry.sub_high)) * rank;
	}

	public Component getTooltip(double val) {
		return MutableComponent.create(new TranslatableContents(
				"attribute.modifier.plus." + (usePercent ? 1 : 0),
				ATTRIBUTE_MODIFIER_FORMAT.format(usePercent ? val * 100 : val),
				MutableComponent.create(new TranslatableContents(attr.get().getDescriptionId())))).withStyle(ChatFormatting.BLUE);
	}

}
