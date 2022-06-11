package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.Multimap;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

	public Optional<ArtifactStats> getStats() {
		return Optional.ofNullable(stats);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
		if (stats != null) {
			return stats.buildAttributes();
		}
		return ICurio.super.getAttributeModifiers(slotContext, uuid);
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips) {
		return new ArrayList<>();
	}

	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void onUnequip(SlotContext slotContext, ItemStack newStack) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().update(slotContext);
		}
	}

	@Override
	public void curioTick(SlotContext slotContext) {
		if (stack.getItem() instanceof BaseArtifact base) {
			base.set.get().tick(slotContext);
		}
	}
}
