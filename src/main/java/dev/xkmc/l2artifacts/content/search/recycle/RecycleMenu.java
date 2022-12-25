package dev.xkmc.l2artifacts.content.search.recycle;

import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class RecycleMenu extends AbstractScrollerMenu<RecycleMenu> {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "recycle");

	public static RecycleMenu fromNetwork(MenuType<RecycleMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new RecycleMenu( wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	public RecycleMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_RECYCLE.get(), wid, plInv, MANAGER, 1, token);
		this.addSlot("input", e -> e.getItem() instanceof ExpItem);
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		reload();
	}

	@Override
	protected void clickSlot(int slot) {
		//TODO
	}

	@Override
	public void slotsChanged(Container cont) {
		super.slotsChanged(cont);
		if (player.getLevel().isClientSide()) return;
		if (!cont.getItem(0).isEmpty()) {
			ItemStack stack = cont.getItem(0).copy();
			//TODO
			cont.setItem(0, ItemStack.EMPTY);
		}
	}

	private void removeSelected() {
		//token.list.remove(selected);
		token.update();
		token.save();
		reload();
	}

}
