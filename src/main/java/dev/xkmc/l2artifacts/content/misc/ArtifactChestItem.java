package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.main.FilteredMenu;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
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

	public static List<ItemStack> getContent(ItemStack stack) {
		var cont = ArtifactItems.ITEMS.getOrDefault(stack, ArtifactChestContents.EMPTY);
		var ans = new ArrayList<ItemStack>();
		for (var e : cont.nonEmptyItems()) {
			ans.add(e);
		}
		return ans;
	}

	public static void setContent(ItemStack stack, List<ItemStack> ans) {
		ArtifactItems.ITEMS.set(stack, ArtifactChestContents.fromItems(ans));
	}

	public ArtifactChestItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	public static int getExp(ItemStack stack) {
		return ArtifactItems.EXP.getOrDefault(stack, 0);
	}

	public static void setExp(ItemStack stack, int exp) {
		ArtifactItems.EXP.set(stack, exp);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player instanceof ServerPlayer sp) {
			int slot = hand == InteractionHand.OFF_HAND ? 40 : player.getInventory().selected;
			ArtifactTypeRegistry.FILTER.type().getOrCreate(player).initFilter(sp);
			new ArtifactChestMenuPvd(FilteredMenu::new, (ServerPlayer) player, slot, stack).open();
		}
		return InteractionResultHolder.success(stack);
	}

	@Override
	public boolean canFitInsideContainerItems() {
		return false;
	}
}
