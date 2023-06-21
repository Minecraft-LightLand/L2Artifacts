package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.swap.ArtifactSwapData;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2itemselector.init.data.L2Keys;
import dev.xkmc.l2itemselector.select.ISelectionListener;
import dev.xkmc.l2itemselector.select.SetSelectedToServer;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class ArtifactSel implements ISelectionListener {

	public static final ArtifactSel INSTANCE = new ArtifactSel();
	public static final int UP = -1;
	public static final int DOWN = -2;
	public static final int SWAP = -3;
	private static final ResourceLocation ID = new ResourceLocation(L2Artifacts.MODID, "swap");


	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean isClientActive(Player player) {
		if (Minecraft.getInstance().screen != null) return false;
		return getData(player) != null;
	}

	@Override
	public void handleServerSetSelection(SetSelectedToServer packet, Player player) {
		var token = getData(player);
		if (token == null) return;
		if (packet.slot == SWAP) {
			token.second().swap(player);
			ArtifactSwapItem.setData(token.first(), token.second());
		} else {
			int s = packet.slot;
			if (packet.slot < 0) {
				s = token.second().select;
				if (packet.slot == UP) s--;
				else s++;
				s = (s + 9) % 9;
			}
			token.second().select = s;
			ArtifactSwapItem.setData(token.first(), token.second());
		}
	}

	@Override
	public boolean handleClientScroll(int i, Player player) {
		if (i < 0) {
			toServer(UP);
			return true;
		} else if (i > 0) {
			toServer(DOWN);
			return true;
		}
		return false;
	}

	@Override
	public void handleClientKey(L2Keys key, Player player) {
		if (key == L2Keys.SWAP) {
			this.toServer(SWAP);
		} else if (key == L2Keys.UP) {
			this.toServer(UP);
		} else if (key == L2Keys.DOWN) {
			this.toServer(DOWN);
		}
	}

	@Override
	public boolean handleClientNumericKey(int i, BooleanSupplier click) {
		if (!Minecraft.getInstance().options.keyShift.isDown()) return false;
		if (click.getAsBoolean()) {
			toServer(i);
			return true;
		}
		return false;
	}

	@Nullable
	public static Pair<ItemStack, ArtifactSwapData> getData(Player player) {
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
