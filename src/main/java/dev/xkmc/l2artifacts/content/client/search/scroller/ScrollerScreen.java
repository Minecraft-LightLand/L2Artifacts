package dev.xkmc.l2artifacts.content.client.search.scroller;

public interface ScrollerScreen {

	ScrollerMenu getMenu();

	int getGuiLeft();

	int getGuiTop();

	void scrollTo(int i);
}
