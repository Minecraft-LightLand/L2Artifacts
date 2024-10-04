package dev.xkmc.l2artifacts.compat.swap;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SetSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SetSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
import dev.xkmc.l2backpack.content.quickswap.set.ArmorSetBagMenu;
import dev.xkmc.l2backpack.content.quickswap.set.ISetToggle;
import dev.xkmc.l2backpack.content.quickswap.set.LongSetToggle;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapTypes;
import dev.xkmc.l2backpack.init.registrate.LBItems;
import dev.xkmc.l2core.base.menu.data.BoolArrayDataSlot;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtifactSwapItem extends SetSwapItem {

	public ArtifactSwapItem(Properties properties) {
		super(properties.stacksTo(1), 5);
	}

	public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
		(new SimpleMenuPvd(player, slot, this, stack, ArtifactSwapMenu::new)).open();
	}

	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		//TODO LBLang.addInfo(flag, list, LBLang.Info.SUIT_BAG_INFO, LBLang.Info.INHERIT);
	}

	public @Nullable IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity player, QuickSwapType type) {
		return type != QuickSwapTypes.ARMOR ? null : new SetSwapToken(this, stack, type);
	}

	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		if (!(stack.getItem() instanceof BaseArtifact base)) return false;
		return slot / 9 == base.slot.get().ordinal();
	}

	public boolean isValidContent(ItemStack stack) {
		return stack.getItem() instanceof BaseArtifact;
	}

	public ISetToggle getToggle(ItemStack stack, @Nullable BoolArrayDataSlot dataSlot) {
		return new LongSetToggle(stack, LBItems.DC_SWAP_TOGGLE, dataSlot);
	}

}
