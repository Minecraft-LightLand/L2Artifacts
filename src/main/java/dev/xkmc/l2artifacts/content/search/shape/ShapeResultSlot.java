package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2library.base.menu.base.PredSlot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ShapeResultSlot extends PredSlot {

	public ShapeResultSlot(Container inv, int ind, int x, int y, Predicate<ItemStack> pred) {
		super(inv, ind, x, y, pred);
	}

	@Override
	public ItemStack remove(int amount) {
		setChanged();
		return super.remove(amount);
	}

}
