package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Random;

public class ArtifactStatType extends NamedEntry<ArtifactStatType> {

	private final Attribute attr;
	private final AttributeModifier.Operation op;

	public ArtifactStatType(Attribute attr, AttributeModifier.Operation op) {
		super(() -> ArtifactRegistry.STAT_TYPE);
		this.attr = attr;
		this.op = op;
	}

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		builder.put(attr, new AttributeModifier(entry.id, entry.name, entry.value, op));
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

}
