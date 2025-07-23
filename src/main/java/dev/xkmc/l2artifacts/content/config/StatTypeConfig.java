package dev.xkmc.l2artifacts.content.config;

import com.google.common.collect.ImmutableMultimap;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

@SerialClass
public class StatTypeConfig extends BaseConfig implements IArtifactFeature.Sprite {

	public static StatTypeConfig get(ResourceLocation id) {
		return NetworkManager.STAT_TYPES.getEntry(id);
	}

	public static Collection<StatTypeConfig> getValues() {
		var list = new ArrayList<>(NetworkManager.STAT_TYPES.getAll());
		list.sort(Comparator.<StatTypeConfig>comparingInt(e -> e.sortIndex)
				.thenComparing(e -> ForgeRegistries.ATTRIBUTES.getKey(e.attr).toString()));
		return list;
	}

	@SerialClass.SerialField
	public double base, base_low, base_high, main_low, main_high, sub_low, sub_high;

	@SerialClass.SerialField
	public Attribute attr;

	@SerialClass.SerialField
	public AttributeModifier.Operation op;

	@SerialClass.SerialField
	public boolean usePercent;

	@SerialClass.SerialField
	public ResourceLocation icon;

	@SerialClass.SerialField
	public int sortIndex;

	public void getModifier(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, StatEntry entry, UUID uuid) {
		builder.put(attr, new AttributeModifier(uuid, entry.getName(), entry.getValue(), op));
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
		var ans = Component.literal("+");
		ans = ans.append(ATTRIBUTE_MODIFIER_FORMAT.format(usePercent ? val * 100 : val));
		if (usePercent) {
			ans = ans.append("%");
		}
		return ans;
	}

	public Component getTooltip(double val) {
		boolean neg = val < 0 ^ AttrTooltip.isNegative(attr);
		return Component.translatable(
				"attribute.modifier.plus." + (usePercent ? 1 : 0),
				ATTRIBUTE_MODIFIER_FORMAT.format(usePercent ? val * 100 : val),
				Component.translatable(attr.getDescriptionId())).withStyle(neg ? ChatFormatting.RED : ChatFormatting.BLUE);
	}

	@Override
	public ResourceLocation getIcon() {
		if (icon != null) return icon;
		ResourceLocation rl = getID();
		return new ResourceLocation(rl.getNamespace(), "textures/stat_type/" + rl.getPath() + ".png");
	}

	@Override
	public MutableComponent getDesc() {
		return Component.translatable("stat_type." + getID().getNamespace() + "." + getID().getPath());
	}

	public double getBaseValue() {
		return base;
	}

}
