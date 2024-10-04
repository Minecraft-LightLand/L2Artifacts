package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.convert.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.convert.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.genesis.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.main.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.upgrade.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record OpenTabToServer(
		int slot, @Nullable Type type
) implements SerialPacketBase<OpenTabToServer> {

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

	public static OpenTabToServer of(ArtifactTabData token, @Nullable Type type) {
		return new OpenTabToServer(token.invSlot, type);
	}

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		ItemStack stack = player.getInventory().getItem(slot);
		if (!(stack.getItem() instanceof ArtifactChestItem))
			return;
		if (type == null) {
			sp.doCloseContainer();
			return;
		}
		var pvd = new ArtifactChestMenuPvd(type.factory, sp, slot, stack);
		if (player.containerMenu instanceof IFilterMenu) {
			ItemStack carried = player.containerMenu.getCarried();
			player.containerMenu.setCarried(ItemStack.EMPTY);
			pvd.open();
			player.containerMenu.setCarried(carried);
		} else {
			pvd.open();
		}
	}

}
