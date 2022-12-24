package dev.xkmc.l2artifacts.content.client.search.screen;

import dev.xkmc.l2artifacts.content.client.search.scroller.ScrollerMenu;
import dev.xkmc.l2artifacts.content.client.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

public class FilteredMenu extends BaseContainerMenu<FilteredMenu> implements ScrollerMenu {

	private static final Comparator<GenericItemStack<BaseArtifact>> COMPARATOR;

	static {
		Comparator<GenericItemStack<BaseArtifact>> c = Comparator.comparingInt(a -> -a.item().rank);
		c = c.thenComparing(a -> a.item().set.get().getID());
		c = c.thenComparing(a -> a.item().slot.get().getID());
		COMPARATOR = c;
	}

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filtered");

	private static int getMaxSize() {
		return 1000;
	}

	public static FilteredMenu fromNetwork(MenuType<FilteredMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int hand = buf.readInt();
		ItemStack stack = plInv.player.getItemInHand(hand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
		return new FilteredMenu(type, wid, plInv, ArtifactChestToken.of(stack));
	}

	private final ArtifactChestToken token;
	private final Player player;

	private ItemStack selected = ItemStack.EMPTY;

	private int max_row, row = 0;

	public FilteredMenu(MenuType<?> type, int wid, Inventory plInv, ArtifactChestToken token) {
		super(type, wid, plInv, MANAGER, e -> new BaseContainer<>(38, e), false);
		this.token = token;
		this.token.setComparator(COMPARATOR);
		this.player = plInv.player;
		this.addSlot("input", e -> token.list.size() < getMaxSize() && (e.getItem() instanceof BaseArtifact || e.getItem() instanceof RandomArtifactItem));
		this.addSlot("output", e -> false);
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		reload();
	}

	private void reload() {
		var list = token.getFiltered();
		max_row = (int) Math.ceil(list.size() / 6.0);
		if (row > getMaxScroll()) row = getMaxScroll();
		if (player.level.isClientSide()) return;
		for (int i = 0; i < 36; i++) {
			int index = row * 6 + i;
			ItemStack stack = index >= list.size() ? ItemStack.EMPTY : list.get(index).stack();
			container.setItem(i + 2, stack);
		}
	}

	public int getMaxScroll() {
		return Math.max(0, max_row - 6);
	}

	public int getScroll() {
		return row;
	}

	@Override
	public boolean clickMenuButton(Player pPlayer, int pId) {
		if (pId == 0) {
			row--;
			reload();
			return true;
		}
		if (pId == 1) {
			row++;
			reload();
			return true;
		}
		pId -= 2;
		pId += row * 6;
		if (pId >= 0 && pId < token.getFiltered().size()) {
			if (!player.getLevel().isClientSide()) {
				selected = token.getFiltered().get(pId).stack();
				container.setItem(1, selected.copy());
			}
			return true;
		}
		return super.clickMenuButton(pPlayer, pId);
	}

	@Override
	public void slotsChanged(Container cont) {
		super.slotsChanged(cont);
		if (player.getLevel().isClientSide()) return;
		if (!cont.getItem(0).isEmpty()) {
			ItemStack stack = cont.getItem(0).copy();
			if (stack.getItem() instanceof RandomArtifactItem item) {
				for (int i = 0; i < stack.getCount(); i++) {
					addItemToList(RandomArtifactItem.getRandomArtifact(item.rank, player.getRandom()));
				}
			} else {
				addItemToList(stack);
			}
			cont.setItem(0, ItemStack.EMPTY);
			token.update();
			token.save();
			selected = ItemStack.EMPTY;
			container.setItem(1, selected);
			reload();
		}
		if (!selected.isEmpty() && cont.getItem(1).isEmpty()) {
			removeSelected();
		}
	}

	private void addItemToList(ItemStack stack) {
		stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
		token.list.add(stack);
	}

	private void removeSelected() {
		token.list.remove(selected);
		token.update();
		token.save();
		selected = ItemStack.EMPTY;
		reload();
	}

	@Override
	public boolean stillValid(Player player) {
		return player.getMainHandItem() == token.stack || player.getOffhandItem() == token.stack;
	}

}
