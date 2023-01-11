package dev.xkmc.l2artifacts.content.search.dissolve;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class DissolveMenu extends AbstractScrollerMenu<DissolveMenu> {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "dissolve");

	public static DissolveMenu fromNetwork(MenuType<DissolveMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new DissolveMenu(wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	protected final DataSlot select_index;

	private int selected = -1;

	public DissolveMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_DISSOLVE.get(), wid, plInv, MANAGER, 2, token, false);
		this.addSlot("input", e -> e.getItem() instanceof StatContainerItem && StatContainerItem.getType(e).isEmpty());
		this.addSlot("output", e -> false);
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		this.select_index = addDataSlot(DataSlot.standalone());
		reload(true);
	}

	@Override
	protected void reload(boolean changeContent) {
		super.reload(changeContent);
		select_index.set(selected - getScroll() * 6);
	}

	private ItemStack setSelected(int slot) {
		selected = slot;
		select_index.set(selected - getScroll() * 6);
		if (selected < 0) return ItemStack.EMPTY;
		var art = token.getFiltered().get(slot);
		ItemStack ans = container.getItem(0).getItem().getDefaultInstance();
		var opt = BaseArtifact.getStats(art.stack());
		if (opt.isEmpty()) return ItemStack.EMPTY;
		StatContainerItem.setStat(ans, opt.get().main_stat.type);
		return ans;
	}

	@Override
	protected void clickSlot(int slot) {
		if (container.getItem(0).isEmpty()) return;
		if (token.getFiltered().get(slot).item().rank != ((StatContainerItem) container.getItem(0).getItem()).rank)
			return;
		container.setItem(1, setSelected(slot));
	}

	@Override
	public void slotsChanged(Container cont) {
		if (!player.getLevel().isClientSide()) {
			if (!cont.getItem(0).isEmpty() && selected >= 0) {
				if (token.getFiltered().get(selected).item().rank != ((StatContainerItem) container.getItem(0).getItem()).rank)
					container.setItem(1, setSelected(-1));
			}
			if (cont.getItem(0).isEmpty() && selected >= 0) {
				container.setItem(1, setSelected(-1));
			}
			if (selected >= 0 && cont.getItem(1).isEmpty()) {
				container.getItem(0).shrink(1);
				removeSelected();
			}
		}
		super.slotsChanged(cont);
	}

	private void removeSelected() {
		token.list.remove(token.getFiltered().get(selected).stack());
		token.update();
		token.save();
		setSelected(-1);
		reload(true);
	}

	@Override
	protected boolean shouldClear(Container container, int slot) {
		return slot == 0;
	}

}
