package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.datapack.DatapackInstance;
import dev.xkmc.l2artifacts.datapack.EntryHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;

public class ArtifactStatTypeHolder extends EntryHolder<ArtifactStatType, ArtifactStatTypeHolder> implements IArtifactFeature.Sprite {

	public ArtifactStatTypeHolder(DatapackInstance<ArtifactStatType, ArtifactStatTypeHolder> registry, ResourceLocation id) {
		super(registry, id);
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(getID().getNamespace(), "textures/stat_type/" + getID().getPath() + ".png");
	}

	public void getModifier(Level level, ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		ArtifactStatType type = get(level);
		builder.put(type.attr, new AttributeModifier(entry.id, entry.getName(), entry.getValue(), type.op));
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
		return (max ? entry.base_high : Mth.nextDouble(random, entry.sub_low, entry.sub_high)) * rank;
	}

	public double getBaseValue() {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return entry.base;
	}

}
