package dev.xkmc.l2artifacts.content.search.fitered;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.tab.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class FilteredMenu extends AbstractScrollerMenu<FilteredMenu> {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filtered");

	public static FilteredMenu fromNetwork(MenuType<FilteredMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		return new FilteredMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
	}

	private ItemStack selected = ItemStack.EMPTY;

	public FilteredMenu(int wid, Inventory plInv, ArtifactTabData token) {
		super(ArtifactMenuRegistry.MT_FILTER.get(), wid, plInv, MANAGER, 2, token, false);
		this.addSlot("input", e -> token.token.list.size() < getMaxSize() &&
				(e.getItem() instanceof BaseArtifact || e.getItem() instanceof RandomArtifactItem));
		this.addSlot("output", e -> false);
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		reload(true);
	}

	private int getMaxSize() {
		return token.token.stack.getItem() == ArtifactItems.FILTER.get() ? ArtifactConfig.COMMON.storageSmall.get() : ArtifactConfig.COMMON.storageLarge.get();
	}

	@Override
	protected void clickSlot(int slot) {
		selected = token.token.getFiltered().get(slot).stack();
		container.setItem(1, selected.copy());
	}

	@Override
	public void slotsChanged(Container cont) {
		if (!player.level().isClientSide()) {
			if (!cont.getItem(0).isEmpty()) {
				ItemStack stack = cont.getItem(0).copy();
				if (stack.getItem() instanceof RandomArtifactItem item) {
					for (int i = 0; i < stack.getCount(); i++) {
						addItemToList(RandomArtifactItem.getRandomArtifact(stack, item.rank, player.getRandom()));
					}
				} else {
					addItemToList(stack);
				}
				cont.setItem(0, ItemStack.EMPTY);
				token.token.update();
				token.token.save();
				selected = ItemStack.EMPTY;
				container.setItem(1, selected);
				reload(true);
			}
			if (!selected.isEmpty() && cont.getItem(1).isEmpty()) {
				removeSelected();
			}
		}
		super.slotsChanged(cont);
	}

	private void addItemToList(ItemStack stack) {
		stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
		token.token.list.add(stack);
	}

	private void removeSelected() {
		token.token.list.remove(selected);
		token.token.update();
		token.token.save();
		selected = ItemStack.EMPTY;
		reload(true);
	}

}
