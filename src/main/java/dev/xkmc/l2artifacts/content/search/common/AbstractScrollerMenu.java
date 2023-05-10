package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.base.menu.scroller.ScrollerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractScrollerMenu<T extends AbstractScrollerMenu<T>> extends BaseContainerMenu<T>
		implements ScrollerMenu, IFilterMenu {

	public final ArtifactChestToken token;
	public final IntDataSlot total_count;
	public final IntDataSlot current_count;
	public final IntDataSlot experience;

	protected final Player player;

	public final int extra;

	private int max_row, row = 0;


	public AbstractScrollerMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, int extra, ArtifactChestToken token, boolean isVirtual) {
		super(type, wid, plInv, manager, e -> new BaseContainer<>(36 + extra, e), isVirtual);
		this.token = token;
		this.player = plInv.player;
		this.extra = extra;
		this.total_count = new IntDataSlot(this);
		this.current_count = new IntDataSlot(this);
		this.experience = new IntDataSlot(this);
	}

	protected void reload(boolean changeContent) {
		var list = token.getFiltered();
		max_row = (int) Math.ceil(list.size() / 6.0);
		if (row < 0) row = 0;
		if (row > getMaxScroll()) row = getMaxScroll();
		if (player.level.isClientSide()) return;
		for (int i = 0; i < 36; i++) {
			int index = row * 6 + i;
			ItemStack stack = index >= list.size() ? ItemStack.EMPTY : list.get(index).stack();
			container.setItem(i + extra, stack);
		}
		total_count.set(token.list.size());
		current_count.set(list.size());
		experience.set(token.exp);
	}

	public final int getMaxScroll() {
		return Math.max(0, max_row - 6);
	}

	public final int getScroll() {
		return row;
	}

	@Override
	public boolean clickMenuButton(Player pPlayer, int pId) {
		int amount = pId / 100;
		pId %= 100;
		if (pId == 0) {
			row -= amount;
			reload(false);
			return true;
		}
		if (pId == 1) {
			row += amount;
			reload(false);
			return true;
		}
		pId -= 2;
		pId += row * 6;
		if (pId >= 0 && pId < token.getFiltered().size()) {
			if (!player.getLevel().isClientSide()) {
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
