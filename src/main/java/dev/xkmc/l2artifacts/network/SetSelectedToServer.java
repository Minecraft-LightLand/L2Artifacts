package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.swap.ArtifactSwapData;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;

@SerialClass
public class SetSelectedToServer extends SerialPacketBase {

	public static final int UP = -1, DOWN = -2, SWAP = -3;

	@SerialClass.SerialField
	private int slot;

	@Deprecated
	public SetSelectedToServer() {

	}

	@OnlyIn(Dist.CLIENT)
	public SetSelectedToServer(int slot) {
		this.slot = slot;
	}

	@Override
	public void handle(NetworkEvent.Context ctx) {
		Player sender = ctx.getSender();
		if (sender == null) return;
		var token = getData(sender);
		if (token == null) return;
		if (slot == SWAP) {
			token.second().swap(sender);
			ArtifactSwapItem.setData(token.first(), token.second());
		} else {
			int s = slot;
			if (slot < 0) {
				s = token.second().select;
				if (slot == -1) s--;
				else s++;
				s = (s + 9) % 9;
			}
			token.second().select = s;
			ArtifactSwapItem.setData(token.first(), token.second());
		}
	}

	@Nullable
	private Pair<ItemStack, ArtifactSwapData> getData(Player player) {
		ItemStack main = player.getMainHandItem();
		if (main.getItem() instanceof ArtifactSwapItem) {
			return Pair.of(main, ArtifactSwapItem.getData(main));
		}
		ItemStack off = player.getOffhandItem();
		if (off.getItem() instanceof ArtifactSwapItem) {
			return Pair.of(off, ArtifactSwapItem.getData(off));
		}
		return null;
	}
}
