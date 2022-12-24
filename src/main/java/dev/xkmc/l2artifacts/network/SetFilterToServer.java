package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.client.search.fitered.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class SetFilterToServer extends SerialPacketBase {

	@SerialClass.SerialField
	private InteractionHand hand;

	@SerialClass.SerialField
	private CompoundTag filter;

	@Deprecated
	public SetFilterToServer() {

	}

	public SetFilterToServer(ArtifactChestToken token) {
		hand = token.hand;
		filter = ArtifactChestItem.getFilter(token.stack);
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null) return;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() != ArtifactItemRegistry.FILTER.get()) return;
		ArtifactChestItem.setFilter(stack, filter);
		new ArtifactChestMenuPvd(player, hand, stack).open();
	}

}
