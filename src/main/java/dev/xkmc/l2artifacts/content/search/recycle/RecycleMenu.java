package dev.xkmc.l2artifacts.content.search.recycle;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.search.common.AbstractScrollerMenu;
import dev.xkmc.l2artifacts.content.search.common.IntDataSlot;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class RecycleMenu extends AbstractScrollerMenu<RecycleMenu> {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "recycle");

	public static RecycleMenu fromNetwork(MenuType<RecycleMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new RecycleMenu(wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	public final IntDataSlot select_count;
	public final IntDataSlot to_gain;

	protected final IntDataSlot sel_0, sel_1;

	protected boolean[] selected;

	public RecycleMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_RECYCLE.get(), wid, plInv, MANAGER, 1, token, true);
		this.addSlot("input", e -> e.getItem() instanceof ExpItem);
		this.addSlot("grid", e -> false, e -> e.setPickup(() -> false));
		select_count = new IntDataSlot(this);
		to_gain = new IntDataSlot(this);
		sel_0 = new IntDataSlot(this);
		sel_1 = new IntDataSlot(this);
		reload(true);
	}

	@Override
	protected void reload(boolean changeContent) {
		super.reload(changeContent);
		if (changeContent) {
			var list = token.getFiltered();
			selected = new boolean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				selected[i] = false;
			}
		}
		refreshSelected();
	}

	private void refreshSelected() {
		int val_0 = 0;
		int val_1 = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				int ind = i * 6 + j;
				int index = getScroll() * 6 + ind;
				boolean sel = index < current_count.get() && selected[index];
				if (!sel) continue;
				if (ind < 18) {
					val_0 |= 1 << ind;
				} else {
					val_1 |= 1 << (ind - 18);
				}
			}
		}
		sel_0.set(val_0);
		sel_1.set(val_1);
	}

	@Override
	protected void clickSlot(int slot) {
		int exp = getExp(slot);
		int s = selected[slot] ? -1 : 1;
		select_count.set(select_count.get() + s);
		to_gain.set(to_gain.get() + s * exp);
		selected[slot] ^= true;
		refreshSelected();
	}

	private int getExp(int i) {
		var list = token.getFiltered();
		ItemStack stack = list.get(i).stack();
		int rank = list.get(i).item().rank;
		var stat = BaseArtifact.getStats(stack).orElse(null);
		return ArtifactUpgradeManager.getExpForConversion(rank, stat);
	}

	@Override
	public void slotsChanged(Container cont) {
		if (!player.getLevel().isClientSide()) {
			if (!cont.getItem(0).isEmpty()) {
				ItemStack stack = cont.getItem(0).copy();
				ExpItem item = (ExpItem) stack.getItem();
				addExp(ArtifactUpgradeManager.getExpForConversion(item.rank, null) * stack.getCount());
				cont.setItem(0, ItemStack.EMPTY);
			}
		}
		super.slotsChanged(cont);
	}

	@Override
	public boolean clickMenuButton(Player player, int pId) {
		if (pId == 50) {
			if (player.level.isClientSide)
				return true;
			var list = token.getFiltered();
			int exp = 0;
			for (int i = 0; i < selected.length; i++) {
				if (selected[i]) {
					ItemStack stack = list.get(i).stack();
					exp += getExp(i);
					token.list.remove(stack);
				}
			}
			addExp(exp);
			select_count.set(0);
			to_gain.set(0);
			token.update();
			token.save();
			reload(true);
			return true;
		}
		if (pId == 51) {
			if (player.level.isClientSide)
				return true;
			var list = token.getFiltered();
			int exp = 0;
			for (int i = 0; i < list.size(); i++) {
				selected[i] = true;
				exp += getExp(i);
			}
			select_count.set(list.size());
			to_gain.set(exp);
			refreshSelected();
			return true;
		}
		if (pId == 52) {
			if (player.level.isClientSide)
				return true;
			var list = token.getFiltered();
			for (int i = 0; i < list.size(); i++) {
				selected[i] = false;
			}
			select_count.set(0);
			to_gain.set(0);
			refreshSelected();
			return true;
		}
		return super.clickMenuButton(player, pId);
	}

	private void addExp(int exp) {
		token.exp += exp;
		ArtifactChestItem.setExp(token.stack, token.exp);
		experience.set(token.exp);
		sendAllDataToRemote();
	}

}
