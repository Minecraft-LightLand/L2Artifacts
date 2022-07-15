package dev.xkmc.l2artifacts.content.upgrades;

import net.minecraft.world.item.Item;

public abstract class UpgradeEnhanceItem extends Item {

	public final int rank;

	public UpgradeEnhanceItem(Properties props, int rank) {
		super(props);
		this.rank = rank;
	}

}
