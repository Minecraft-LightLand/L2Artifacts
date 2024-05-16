package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.Supplier;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ArtifactStatType extends NamedEntry<ArtifactStatType> implements IArtifactFeature.Sprite {

	@Deprecated
	public final Supplier<Attribute> attr;
	@Deprecated
	public final AttributeModifier.Operation op;
	@Deprecated
	public final boolean usePercent;

	public ArtifactStatType(Supplier<Attribute> attr, AttributeModifier.Operation op, boolean useMult) {
		super(ArtifactTypeRegistry.STAT_TYPE);
		this.attr = attr;
		this.op = op;
		this.usePercent = useMult;
	}

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		builder.put(getAttr().get(), new AttributeModifier(entry.id, entry.getName(), entry.value, getOp()));
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

	public MutableComponent getValueText(double val) {
		var ans = Component.literal("+");
		ans = ans.append(ATTRIBUTE_MODIFIER_FORMAT.format(isUsePercent() ? val * 100 : val));
		if (isUsePercent()) {
			ans = ans.append("%");
		}
		return ans;
	}

	public Component getTooltip(double val) {
		return MutableComponent.create(new TranslatableContents(
				"attribute.modifier.plus." + (isUsePercent() ? 1 : 0),
				ATTRIBUTE_MODIFIER_FORMAT.format(isUsePercent() ? val * 100 : val),
				MutableComponent.create(new TranslatableContents(getAttr().get().getDescriptionId())))).withStyle(ChatFormatting.BLUE);
	}

	@Override
	public ResourceLocation getIcon() {
		ResourceLocation rl = getRegistryName();
		return new ResourceLocation(rl.getNamespace(), "textures/stat_type/" + rl.getPath() + ".png");
	}

	public Supplier<Attribute> getAttr() {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return () -> entry.attr;
	}

	public boolean isUsePercent() {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return entry.usePercent;
	}

	public AttributeModifier.Operation getOp() {
		StatTypeConfig.Entry entry = StatTypeConfig.getInstance().stats.get(this);
		return entry.op;
	}
}
