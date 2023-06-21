package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.base.menu.scroller.ScrollerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractScrollerMenu<T extends AbstractScrollerMenu<T>> extends BaseContainerMenu<T>
		implements ScrollerMenu, IFilterMenu {

	public final ArtifactChestToken token;
	public final IntDataSlot total_count;
	public final IntDataSlot current_count;
	public final IntDataSlot experience;
	public final DataSlot max_row;
	public final DataSlot row;


	protected final Player player;

	public final int extra;


	public AbstractScrollerMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, int extra, ArtifactChestToken token, boolean isVirtual) {
		super(type, wid, plInv, manager, e -> new BaseContainer<>(36 + extra, e), isVirtual);
		this.token = token;
		this.player = plInv.player;
		this.extra = extra;
		this.total_count = new IntDataSlot(this);
		this.current_count = new IntDataSlot(this);
		this.experience = new IntDataSlot(this);
		this.max_row = addDataSlot(DataSlot.standalone());
		this.row = addDataSlot(DataSlot.standalone());
	}

	protected void reload(boolean changeContent) {
		if (player.level().isClientSide()) return;
		var list = token.getFiltered();
		max_row.set((int) Math.ceil(list.size() / 6.0));
		if (row.get() < 0) row.set(0);
		if (row.get() > getMaxScroll()) row.set(getMaxScroll());
		for (int i = 0; i < 36; i++) {
			int index = row.get() * 6 + i;
			ItemStack stack = index >= list.size() ? ItemStack.EMPTY : list.get(index).stack();
			container.setItem(i + extra, stack);
		}
		total_count.set(token.list.size());
		current_count.set(list.size());
		experience.set(token.exp);
	}

	public final int getMaxScroll() {
		return Math.max(0, max_row.get() - 6);
	}

	public final int getScroll() {
		return row.get();
	}

	@Override
	public boolean clickMenuButton(Player pPlayer, int pId) {
		int amount = pId / 100;
		pId %= 100;
		if (pId == 0) {
			row.set(row.get() - amount);
			reload(false);
			return true;
		}
		if (pId == 1) {
			row.set(row.get() + amount);
			reload(false);
			return true;
		}
		pId -= 2;
		pId += row.get() * 6;
		if (pId >= 0 && pId < token.getFiltered().size()) {
			if (!player.level().isClientSide()) {
				clickSlot(pId);
			}
			return true;
		}
		return super.clickMenuButton(pPlayer, pId);
	}

	protected abstract void clickSlot(int slot);

	@Override
	public final boolean stillValid(Player player) {
		return player.getInventory().getItem(token.invSlot) == token.stack;
	}

	@Override
	protected boolean shouldClear(Container container, int slot) {
		return slot < extra;
	}
}
