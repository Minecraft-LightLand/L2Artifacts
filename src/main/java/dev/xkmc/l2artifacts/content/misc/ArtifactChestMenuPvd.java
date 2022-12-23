package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.client.search.screen.FilteredMenu;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public final class ArtifactChestMenuPvd implements MenuProvider {

	private final ServerPlayer player;
	private final InteractionHand hand;
	private final ItemStack stack;

	public ArtifactChestMenuPvd(ServerPlayer player, InteractionHand hand, ItemStack stack) {
		this.player = player;
		this.hand = hand;
		this.stack = stack;
	}

	@Override
	public Component getDisplayName() {
		return stack.getDisplayName();
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new FilteredMenu(ArtifactMenuRegistry.MT_FILTER.get(), id, inventory, ArtifactChestToken.of(stack));
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		buf.writeInt(hand == InteractionHand.MAIN_HAND ? 0 : 1);
	}

	public void open() {
		NetworkHooks.openScreen(player, this, this::writeBuffer);
	}

}
