package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.tab.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record SetFilterToServer(
		int slot, CompoundTag filter, @Nullable Type type
) implements SerialPacketBase<SetFilterToServer> {

	public enum Type {
		FILTER(FilteredMenu::new),
		RECYCLE(RecycleMenu::new),
		UPGRADE(UpgradeMenu::new),
		DISSOLVE(DissolveMenu::new),
		AUGMENT(AugmentMenu::new),
		SHAPE(ShapeMenu::new);

		private final ArtifactChestMenuPvd.Factory factory;

		Type(ArtifactChestMenuPvd.Factory factory) {
			this.factory = factory;
		}
	}

	public static SetFilterToServer of(ArtifactTabData token, @Nullable Type type) {
		var slot = token.token.invSlot;
		var filter = TagCodec.toTag(new CompoundTag(), token);
		ArtifactChestItem.setFilter(Proxy.getClientPlayer().getInventory().getItem(slot), filter);
		return new SetFilterToServer(slot, filter, type);
	}

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		ItemStack stack = player.getInventory().getItem(slot);
		if (!(stack.getItem() instanceof ArtifactChestItem))
			return;
		ArtifactChestItem.setFilter(stack, filter);
		if (type == null) {
			sp.doCloseContainer();
			return;
		}
		if (player.containerMenu instanceof IFilterMenu) {
			ItemStack carried = player.containerMenu.getCarried();
			player.containerMenu.setCarried(ItemStack.EMPTY);
			new ArtifactChestMenuPvd(type.factory, sp, slot, stack).open();
			player.containerMenu.setCarried(carried);
		} else {
			new ArtifactChestMenuPvd(type.factory, sp, slot, stack).open();
		}
	}

}
