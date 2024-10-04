package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.filter.ArtifactFilterData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArtifactTabData extends TabGroupData<ArtifactTabData> {

	public static ArtifactTabData of(Player player, int invSlot) {
		ItemStack stack = player.getInventory().getItem(invSlot);
		return new ArtifactTabData(stack.is(ArtifactItems.UPGRADED_POCKET), invSlot, stack);
	}

	public final boolean advanced;
	public final ArtifactFilterData filter;
	public final FilledTokenData data;

	private final ItemStack stack;
	public final int invSlot;

	public int exp = 0;

	private final List<TabToken<ArtifactTabData, ?>> list;

	public ArtifactTabData(boolean advanced, int invSlot, ItemStack stack) {
		super(ArtifactTabRegistry.ARTIFACT);
		this.advanced = advanced;
		list = new ArrayList<>();
		for (var e : advanced ? ArtifactTabRegistry.LIST_1 : ArtifactTabRegistry.LIST_0) {
			list.add(e.get());
		}

		this.invSlot = invSlot;
		this.stack = stack;

		exp = ArtifactChestItem.getExp(stack);
		filter = new ArtifactFilterData();
		data = new FilledTokenData(ArtifactChestItem.getContent(stack));
	}

	@Override
	public List<TabToken<ArtifactTabData, ?>> getTabs() {
		return list;
	}

	public ArtifactTabData copy() {
		return of(Proxy.getPlayer(), invSlot);
	}

	public boolean isAvailable(int i, int j, boolean sort) {
		var f = filter.filters.get(i);
		return sort && f.getSelected(j) || f.getAvailability(data, j);
	}

	public void prioritize(int i) {
		filter.prioritize(i);
	}

	public void prioritize(int i, int j) {
		filter.filters.get(i).prioritize(j);
	}

	public void toggleSel(int i, int j) {
		filter.filters.get(i).toggle(data, j);
	}

	public void setAllSel(int type, boolean selected) {
		var f = filter.filters.get(type);
		for (int i = f.allEntries.size() - 1; i >= 0; i--) {
			if (f.getSelected(i) != selected) {
				f.toggle(data, i);
			}
		}
	}

	public Component getDescription(int i) {
		return filter.filters.get(i).getDescription();
	}

	public List<GenericItemStack<BaseArtifact>> getFiltered() {
		return data.getFiltered(filter);
	}

	public void remove(ItemStack stack) {
		data.list.remove(stack);

	}

	public void updateAndSave() {
		filter.update(data);
		ArtifactChestItem.setContent(stack, data.list);
	}

	public void getFilteredToServer() {
		//TODO
	}

	public void addExp(int val) {
		exp += val;
		ArtifactChestItem.setExp(stack, exp);
	}

	public boolean stillValid(Player player) {
		return stack == player.getInventory().getItem(invSlot);
	}

}
