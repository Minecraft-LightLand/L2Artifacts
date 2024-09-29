package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.misc.SelectArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public record ChooseArtifactToServer(
		int set, int slot, int rank
) implements SerialPacketBase<ChooseArtifactToServer> {

	@Override
	public void handle(Player pl) {
		if (!(pl instanceof ServerPlayer player)) return;
		ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!(stack.getItem() instanceof SelectArtifactItem)) {
			stack = player.getItemInHand(InteractionHand.OFF_HAND);
		}
		if (!(stack.getItem() instanceof SelectArtifactItem)) {
			return;
		}
		var sets = L2Artifacts.REGISTRATE.SET_LIST;
		if (set >= sets.size()) return;
		var slots = sets.get(set).items;
		if (slot >= slots.length) return;
		var ranks = slots[slot];
		if (rank >= ranks.length) return;
		if (!player.getAbilities().instabuild)
			stack.shrink(1);
		ItemStack artifact = ranks[rank].asStack();
		player.getInventory().placeItemBackInInventory(artifact);
	}
}
