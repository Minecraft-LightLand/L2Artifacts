package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public record ArtifactSwapMenuPvd(ServerPlayer player, int slot, ItemStack stack) implements MenuProvider {

	@Override
	public Component getDisplayName() {
		return stack.getHoverName();
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new ArtifactSwapMenu(ArtifactMenuRegistry.MT_SWAP.get(), id, inventory, slot);
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		buf.writeInt(slot);
	}

	public void open() {
		player.openMenu(this, this::writeBuffer);
	}

}
