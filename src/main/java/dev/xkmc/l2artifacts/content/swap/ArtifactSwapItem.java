package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2library.serial.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArtifactSwapItem extends Item {

	private static final String KEY = "SwapData";

	public static ArtifactSwapData getData(ItemStack stack) {
		ArtifactSwapData data = null;
		if (stack.getOrCreateTag().contains(KEY)) {
			data = TagCodec.fromTag(stack.getOrCreateTag().getCompound(KEY), ArtifactSwapData.class);
		}
		if (data == null) {
			return new ArtifactSwapData();
		}
		return data;
	}

	public ArtifactSwapItem(Properties properties) {
		super(properties);
	}

	public static void setData(ItemStack stack, ArtifactSwapData data) {
		CompoundTag tag = TagCodec.toTag(new CompoundTag(), data);
		if (tag == null) return;
		stack.getOrCreateTag().put(KEY, tag);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			new ArtifactSwapMenuPvd((ServerPlayer) player, hand, stack).open();
		}
		return InteractionResultHolder.success(stack);
	}

}
