package dev.xkmc.l2artifacts.content.search.tabs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class FilterTabToken<T extends FilterTabBase<T>> {

	public interface TabFactory<T extends FilterTabBase<T>> {

		T create(FilterTabToken<T> token, FilterTabManager manager, ItemStack stack, Component component);

	}

	public final int index;
	public final TabFactory<T> factory;
	public final FilterTabType type;

	private final Supplier<Item> item;
	private final Component title;

	public FilterTabToken(int index, TabFactory<T> factory, Supplier<Item> item, Component component) {
		this.index = index;
		this.factory = factory;
		this.type = FilterTabType.RIGHT;

		this.item = item;
		this.title = component;
	}

	public T create(FilterTabManager manager) {
		return factory.create(this, manager, item.get().getDefaultInstance(), title);
	}
}
