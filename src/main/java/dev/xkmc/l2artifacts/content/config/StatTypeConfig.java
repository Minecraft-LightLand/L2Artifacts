package dev.xkmc.l2artifacts.content.config;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Collection;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@SerialClass
public class StatTypeConfig extends BaseConfig implements IArtifactFeature.Sprite {

	public static StatTypeConfig get(ResourceLocation id) {
		return NetworkManager.STAT_TYPES.getEntry(id);
	}

	public static Collection<StatTypeConfig> getValues() {
		return NetworkManager.STAT_TYPES.getAll();
	}

	@SerialClass.SerialField
	public double base, base_low, base_high, main_low, main_high, sub_low, sub_high;

	@SerialClass.SerialField
	public Attribute attr;

	@SerialClass.SerialField
	public AttributeModifier.Operation op;

	@SerialClass.SerialField
	public boolean usePercent;

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry) {
		builder.put(attr, new AttributeModifier(entry.id, entry.getName(), entry.getValue(), op));
	}

	public double getInitialValue(int rank, RandomSource random, boolean max) {
		return (max ? base_high : Mth.nextDouble(random, base_low, base_high)) * rank;
	}

	public double getMainValue(int rank, RandomSource random, boolean max) {
		return (max ? main_high : Mth.nextDouble(random, main_low, main_high)) * rank;
	}

	public double getSubValue(int rank, RandomSource random, boolean max) {
		return (max ? base_high : Mth.nextDouble(random, sub_low, sub_high)) * rank;
	}

	public MutableComponent getValueText(double val) {
		var ans = Component.literal("+");
		ans = ans.append(ATTRIBUTE_MODIFIER_FORMAT.format(usePercent ? val * 100 : val));
		if (usePercent) {
			ans = ans.append("%");
		}
		return ans;
	}

	public Component getTooltip(double val) {
		return Component.translatable(
				"attribute.modifier.plus." + (usePercent ? 1 : 0),
				ATTRIBUTE_MODIFIER_FORMAT.format(usePercent ? val * 100 : val),
				Component.translatable(attr.getDescriptionId())).withStyle(ChatFormatting.BLUE);
	}

	@Override
	public ResourceLocation getIcon() {
		ResourceLocation rl = getID();
		return new ResourceLocation(rl.getNamespace(), "textures/stat_type/" + rl.getPath() + ".png");
	}

	@Override
	public MutableComponent getDesc() {
		return Component.translatable("stat_type." + getID().getPath() + "." + getID().getPath());
	}

	public double getBaseValue() {
		return base;
	}

}
