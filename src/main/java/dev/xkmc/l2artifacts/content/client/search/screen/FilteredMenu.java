package dev.xkmc.l2artifacts.content.client.search.screen;

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

public class FilteredMenu extends BaseContainerMenu<FilteredMenu> {

	private static final Comparator<GenericItemStack<BaseArtifact>> COMPARATOR;
	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "filtered");

	static {
		Comparator<GenericItemStack<BaseArtifact>> c = Comparator.comparingInt(a -> -a.item().rank);
		c = c.thenComparing(a -> a.item().set.get().getID());
		c = c.thenComparing(a -> a.item().slot.get().getID());
		COMPARATOR = c;
	}

	public static FilteredMenu fromNetwork(MenuType<FilteredMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int hand = buf.readInt();
		ItemStack stack = plInv.player.getItemInHand(hand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
		return new FilteredMenu(type, wid, plInv, ArtifactChestToken.of(stack));
	}

	private final ArtifactChestToken token;
	private final Player player;

	private ItemStack selected = ItemStack.EMPTY;

	public FilteredMenu(MenuType<?> type, int wid, Inventory plInv, ArtifactChestToken token) {
		super(type, wid, plInv, MANAGER, e -> new BaseContainer<>(2, e), false);
		this.token = token;
		this.player = plInv.player;
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		this.addSlot("input", e -> e.getItem() instanceof BaseArtifact || e.getItem() instanceof RandomArtifactItem);
		this.addSlot("output", e -> false, e -> e.setTake(this::removeSelected));
		reload();
	}

	private void reload() {
		if (player.level.isClientSide()) return;
		var list = token.getFiltered();
		list.sort(COMPARATOR);
		for (int i = 0; i < list.size(); i++) {
			container.setItem(i, list.get(i).stack());
		}
	}

	@Override
	public boolean clickMenuButton(Player pPlayer, int pId) {
		if (pId >= 0 && pId < token.list.size()) selected = token.list.get(pId);
		container.setItem(1, selected.copy());
		return super.clickMenuButton(pPlayer, pId);
	}

	@Override
	public void slotsChanged(Container cont) {
		super.slotsChanged(cont);
		if (player.getLevel().isClientSide()) return;
		if (!cont.getItem(0).isEmpty()) {
			ItemStack stack = cont.getItem(0).copy();
			if (stack.getItem() instanceof RandomArtifactItem item) {
				stack = RandomArtifactItem.getRandomArtifact(item.rank, player.getRandom());
			}
			stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
			cont.setItem(0, ItemStack.EMPTY);
			addItemToList(stack);
		}
	}

	private void addItemToList(ItemStack stack) {
		if (stack.isEmpty() || player.level.isClientSide()) return;
		token.list.add(stack);
		token.update();
		token.save();
		selected = ItemStack.EMPTY;
		container.setItem(1, selected);
		reload();
	}

	private void removeSelected(ItemStack stack) {
		if (selected.isEmpty()) return;
		token.list.remove(selected);
		token.update();
		token.save();
		selected = ItemStack.EMPTY;
		container.setItem(1, selected);
		reload();
	}

}
