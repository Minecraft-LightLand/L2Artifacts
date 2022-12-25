package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ArtifactChestItem extends Item {

	private static final String KEY_LIST = "artifact_list";
	private static final String KEY_FILTER = "filter";

	public static List<ItemStack> getContent(ItemStack stack) {
		var list = ItemCompoundTag.of(stack).getSubList(KEY_LIST, Tag.TAG_COMPOUND).getOrCreate();
		List<ItemStack> ans = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ans.add(ItemStack.of(list.getCompound(i)));
		}
		return ans;
	}

	public static void setContent(ItemStack stack, List<ItemStack> ans) {
		var list = ItemCompoundTag.of(stack).getSubList(KEY_LIST, Tag.TAG_COMPOUND);
		list.clear();
		for (ItemStack e : ans) {
			list.addCompound().setTag(e.serializeNBT());
		}
	}

	public ArtifactChestItem(Properties properties) {
		super(properties);
	}

	public static CompoundTag getFilter(ItemStack stack) {
		return stack.getOrCreateTag().getCompound(KEY_FILTER);
	}

	public static void setFilter(ItemStack stack, CompoundTag filter) {
		stack.getOrCreateTag().put(KEY_FILTER, filter);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			new ArtifactChestMenuPvd(FilteredMenu::new, (ServerPlayer) player, hand, stack).open();
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
}
