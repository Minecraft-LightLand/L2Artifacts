package dev.xkmc.l2artifacts.content.search.scroller;

public interface ScrollerScreen {

	ScrollerMenu getMenu();

	int getGuiLeft();

	int getGuiTop();

	void scrollTo(int i);
}
