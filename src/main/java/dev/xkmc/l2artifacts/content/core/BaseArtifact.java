package dev.xkmc.l2artifacts.content.core;

import com.google.common.collect.Multimap;
import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BaseArtifact extends Item {

	public static final String KEY = "ArtifactData";

	public final Supplier<ArtifactSlot> slot;
	public final int rank;

	public BaseArtifact(Properties properties, Supplier<ArtifactSlot> slot, int rank) {
		super(properties.stacksTo(1));
		this.slot = slot;
		this.rank = rank;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains(KEY)) {
			ArtifactStats stats = TagCodec.fromTag(stack.getTag().getCompound(KEY), ArtifactStats.class);
			return stats.buildAttributes();
		}
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getTag() == null || !stack.getTag().contains(KEY)) {
			if (!level.isClientSide()) {
				ArtifactStats stats = new ArtifactStats(slot.get(), rank, level.random);
				stack.getOrCreateTag().put(KEY, TagCodec.toTag(new CompoundTag(), stats));
			}
			return InteractionResultHolder.success(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	public void upgrade(ItemStack stack, int exp, Random random) {
		if (stack.getTag() != null && stack.getTag().contains(KEY)) {
			ArtifactStats stats = TagCodec.fromTag(stack.getTag().getCompound(KEY), ArtifactStats.class);
			stats.addExp(exp, random);
			stack.getTag().put(KEY, TagCodec.toTag(new CompoundTag(), stats));
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		//TODO
		super.appendHoverText(stack, level, list, flag);
	}
}
