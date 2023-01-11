package dev.xkmc.l2artifacts.content.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RankedItem extends Item {

	protected static Rarity getRarity(int rank) {
		return rank <= 2 ? Rarity.UNCOMMON : rank <= 4 ? Rarity.RARE : Rarity.EPIC;
	}

	public final int rank;

	public RankedItem(Properties props, int rank) {
		super(props.rarity(getRarity(rank)));
		this.rank = rank;
	}

}
