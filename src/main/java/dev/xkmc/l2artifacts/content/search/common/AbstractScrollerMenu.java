package dev.xkmc.l2artifacts.content.search.common;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.scroller.ScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public abstract class AbstractScrollerMenu<T extends AbstractScrollerMenu<T>> extends BaseContainerMenu<T> implements ScrollerMenu, IFilterMenu {

	public static final Comparator<GenericItemStack<BaseArtifact>> COMPARATOR;

	static {
		Comparator<GenericItemStack<BaseArtifact>> c = Comparator.comparingInt(a -> -a.item().rank);
		c = c.thenComparing(a -> a.item().set.get().getID());
		c = c.thenComparing(a -> a.item().slot.get().getID());
		COMPARATOR = c;
	}

	public final ArtifactChestToken token;
	public final DataSlot total_count;
	public final DataSlot current_count;
	public final DataSlot experience;

	protected final Player player;

	public final int extra;

	private int max_row, row = 0;


	public AbstractScrollerMenu(MenuType<?> type, int wid, Inventory plInv, SpriteManager manager, int extra, ArtifactChestToken token) {
		super(type, wid, plInv, manager, e -> new BaseContainer<>(36 + extra, e), false);
		this.token = token;
		this.token.setComparator(COMPARATOR);
		this.player = plInv.player;
		this.extra = extra;
		this.total_count = addDataSlot(DataSlot.standalone());
		this.current_count = addDataSlot(DataSlot.standalone());
		this.experience = addDataSlot(DataSlot.standalone());
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
		return player.getItemInHand(token.hand) == token.stack;
	}

}
