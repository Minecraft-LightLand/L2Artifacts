package dev.xkmc.l2artifacts.compat.swap;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2backpack.content.quickswap.set.GenericSetSwapMenu;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2menustacker.screen.source.PlayerSlot;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

import java.util.UUID;

public class ArtifactSwapMenu extends GenericSetSwapMenu<ArtifactSwapMenu> {

	public static ArtifactSwapMenu fromNetwork(MenuType<ArtifactSwapMenu> type, int wid, Inventory plInv, RegistryFriendlyByteBuf buf) {
		PlayerSlot<?> slot = PlayerSlot.read(buf);
		UUID id = buf.readUUID();
		return new ArtifactSwapMenu(wid, plInv, slot, id, Component.empty());
	}

	public static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "swap");

	protected ArtifactSwapMenu(int wid, Inventory plInv, PlayerSlot<?> hand, UUID uuid, Component title) {
		super(ArtifactMenuRegistry.MT_SWAP.get(), wid, plInv, MANAGER, hand, uuid, 5);
	}


}
