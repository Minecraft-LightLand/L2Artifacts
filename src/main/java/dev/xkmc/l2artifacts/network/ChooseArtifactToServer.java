package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.misc.SelectArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ChooseArtifactToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public int set, slot, rank;

	@Deprecated
	public ChooseArtifactToServer() {

	}

	public ChooseArtifactToServer(int set, int slot, int rank) {
		this.set = set;
		this.slot = slot;
		this.rank = rank;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null) return;
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
