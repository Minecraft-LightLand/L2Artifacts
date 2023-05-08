package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.content.search.common.IntDataSlot;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapMenu extends BaseContainerMenu<ArtifactSwapMenu> {

	public static ArtifactSwapMenu fromNetwork(MenuType<ArtifactSwapMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new ArtifactSwapMenu(type, wid, plInv, hand);
	}

	public static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "swap");

	private final InteractionHand hand;
	private final ItemStack stack;

	public final ArtifactSwapData data;

	public final IntDataSlot upper = new IntDataSlot(this), lower = new IntDataSlot(this);

	private boolean init = false;

	protected ArtifactSwapMenu(MenuType<?> type, int wid, Inventory plInv, InteractionHand hand) {
		super(type, wid, plInv, MANAGER, e -> new BaseContainer<>(45, e), false);
		this.hand = hand;
		stack = plInv.player.getItemInHand(hand);
		data = ArtifactSwapItem.getData(stack);
		//TODO remove redundant func after bug fixed in lib-2.2.0
		addSlot("grid", (i, st) -> data.contents[i].canAccept(st), (a, b) -> {
		});
		reload();
		init = true;
	}

	private void reload() {
		int low = 0, up = 0;
		for (int i = 0; i < 45; i++) {
			container.setItem(i, data.contents[i].getStack().copy());
			if (data.contents[i].isLocked()) {
				if (i < 30) low |= 1 << i;
				else up |= 1 << (i - 30);
			}
		}
		lower.set(low);
		upper.set(up);
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (id >= 0 && id < 45) {
			if (!player.level.isClientSide()) {
				data.contents[id].toggle();
				save();
				reload();
			}
			return true;
		}
		return false;
	}

	private void save() {
		if (!init || inventory.player.level.isClientSide()) {
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
		return player.getItemInHand(hand) == stack;
	}

}
