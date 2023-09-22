package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ArtifactStatType {

	public static final Codec<ArtifactStatType> CODEC = RecordCodecBuilder.create(i -> i.group(
			ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("attr").forGetter(e -> e.attr),
			Codec.STRING.fieldOf("operation").forGetter(e -> e.op.name()),
			Codec.BOOL.fieldOf("percent").forGetter(e -> e.usePercent)
	).apply(i, ArtifactStatType::new));

	public final Attribute attr;
	protected final AttributeModifier.Operation op;
	private final boolean usePercent;

	public ArtifactStatType(Attribute attr, String op, boolean useMult) {
		this.attr = attr;
		this.op = AttributeModifier.Operation.valueOf(op);
		this.usePercent = useMult;
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

}
