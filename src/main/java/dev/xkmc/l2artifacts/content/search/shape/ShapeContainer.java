package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2library.base.menu.BaseContainerMenu;

public class ShapeContainer extends BaseContainerMenu.BaseContainer<ShapeMenu> {

	public ShapeContainer(int size, ShapeMenu menu) {
		super(size, menu);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

}
