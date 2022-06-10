package dev.xkmc.l2artifacts.content.capability;

import com.google.common.collect.Multimap;
import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.UUID;

public class ArtifactCurioCap implements ICurio {

	private final ItemStack stack;

	private ArtifactStats stats;

	public ArtifactCurioCap(ItemStack stack) {
		this.stack = stack;
		if (stack.getTag() != null && stack.getTag().contains(BaseArtifact.KEY)) {
			stats = TagCodec.fromTag(stack.getTag().getCompound(BaseArtifact.KEY), ArtifactStats.class);
		}
	}

	@Override
	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
		if (stats != null) {
			return stats.buildAttributes();
		}
		return ICurio.super.getAttributeModifiers(slotContext, uuid);
	}

}
