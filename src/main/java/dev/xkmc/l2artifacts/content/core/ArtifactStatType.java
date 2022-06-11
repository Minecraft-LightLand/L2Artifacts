package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Random;
import java.util.function.Supplier;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ArtifactStatType extends NamedEntry<ArtifactStatType> {

	private final Supplier<Attribute> attr;
	private final AttributeModifier.Operation op;
	private final boolean useMult;

	public ArtifactStatType(Supplier<Attribute> attr, AttributeModifier.Operation op, boolean useMult) {
		super(() -> ArtifactRegistry.STAT_TYPE);
		this.attr = attr;
		this.op = op;
		this.useMult = useMult;
	}

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		builder.put(attr.get(), new AttributeModifier(entry.id, entry.name, entry.value, op));
	}

	public double getInitialValue(ArtifactStats stat, Random random) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return random.nextDouble(entry.base_low, entry.base_high) * stat.rank;
	}

	public double getMainValue(ArtifactStats stat, Random random) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return random.nextDouble(entry.main_low, entry.main_high) * stat.rank;
	}

	public double getSubValue(ArtifactStats stat, Random random) {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return random.nextDouble(entry.sub_low, entry.sub_high) * stat.rank;
	}

	public Component getTooltip(double val) {
		return new TranslatableComponent(
				"attribute.modifier.plus." + (useMult ? 1 : 0),
				ATTRIBUTE_MODIFIER_FORMAT.format(useMult ? val * 100 : val),
				new TranslatableComponent(attr.get().getDescriptionId())).withStyle(ChatFormatting.BLUE);
	}

}
