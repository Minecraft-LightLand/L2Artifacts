package dev.xkmc.l2artifacts.content.search.tabs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class FilterTabToken<T extends FilterTabBase<T>> {

	public interface TabFactory<T extends FilterTabBase<T>> {

		T create(int index, FilterTabToken<T> token, FilterTabManager manager, ItemStack stack, Component component);

	}

	public final TabFactory<T> factory;
	public final FilterTabType type;

	private final Supplier<Item> item;
	private final Component title;

	public FilterTabToken(TabFactory<T> factory, Supplier<Item> item, Component component) {
		this.factory = factory;
		this.type = FilterTabType.RIGHT;

		this.item = item;
		this.title = component;
	}

	public T create(int index, FilterTabManager manager) {
		return factory.create(index, this, manager, item.get().getDefaultInstance(), title);
	}
}
