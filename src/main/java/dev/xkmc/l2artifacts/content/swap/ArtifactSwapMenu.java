package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.BoolArrayDataSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapMenu extends BaseContainerMenu<ArtifactSwapMenu> {

	public static ArtifactSwapMenu fromNetwork(MenuType<ArtifactSwapMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		return new ArtifactSwapMenu(type, wid, plInv, i);
	}

	public static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "swap");

	private final int slot;
	private final ItemStack stack;

	public final ArtifactSwapData data;

	public final BoolArrayDataSlot disable = new BoolArrayDataSlot(this, 45);

	private boolean init = false;

	protected ArtifactSwapMenu(MenuType<?> type, int wid, Inventory plInv, int slot) {
		super(type, wid, plInv, MANAGER, e -> new BaseContainer<>(45, e), false);
		this.slot = slot;
		stack = plInv.player.getInventory().getItem(slot);
		data = ArtifactSwapItem.getData(stack);
		addSlot("grid", (i, st) -> data.contents[i].canAccept(st));
		reload();
		init = true;
	}

	private void reload() {
		for (int i = 0; i < 45; i++) {
			container.setItem(i, data.contents[i].getStack().copy());
			disable.set(data.contents[i].isLocked(), i);
		}
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id >= 0 && id < 45) {
			if (!player.level().isClientSide()) {
				data.contents[id].toggle();
				save();
				reload();
			}
			return true;
		}
		return false;
	}

	private void save() {
		if (!init || inventory.player.level().isClientSide()) {
			return;
		}
		for (int i = 0; i < 45; i++) {
			data.contents[i].setStack(container.getItem(i).copy());
		}
		ArtifactSwapItem.setData(stack, data);
	}

	@Override
	protected void securedServerSlotChange(Container cont) {
		save();
	}

	@Override
	public boolean stillValid(Player player) {
		return player.getInventory().getItem(slot) == stack;
	}

}
