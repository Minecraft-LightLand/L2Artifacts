package dev.xkmc.l2artifacts.content.misc;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public final class ArtifactChestContents {
	public static final ArtifactChestContents EMPTY = new ArtifactChestContents(NonNullList.create());
	public static final Codec<ArtifactChestContents> CODEC;
	public static final StreamCodec<RegistryFriendlyByteBuf, ArtifactChestContents> STREAM_CODEC;
	private final NonNullList<ItemStack> items;
	private final int hashCode;

	private ArtifactChestContents(NonNullList<ItemStack> items) {
		this.items = items;
		this.hashCode = ItemStack.hashStackList(items);
	}

	private ArtifactChestContents(int size) {
		this(NonNullList.withSize(size, ItemStack.EMPTY));
	}

	private ArtifactChestContents(List<ItemStack> items) {
		this(items.size());

		for (int i = 0; i < items.size(); ++i) {
			this.items.set(i, items.get(i));
		}

	}

	public static ArtifactChestContents fromItems(List<ItemStack> items) {
		int i = findLastNonEmptySlot(items);
		if (i == -1) {
			return EMPTY;
		} else {
			ArtifactChestContents c = new ArtifactChestContents(i + 1);
			for (int j = 0; j <= i; ++j) {
				c.items.set(j, (items.get(j)).copy());
			}

			return c;
		}
	}

	private static int findLastNonEmptySlot(List<ItemStack> items) {
		for (int i = items.size() - 1; i >= 0; --i) {
			if (!items.get(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}

	public Stream<ItemStack> stream() {
		return this.items.stream().map(ItemStack::copy);
	}

	public Iterable<ItemStack> nonEmptyItems() {
		return Iterables.filter(this.items, (e) -> !e.isEmpty());
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			if (other instanceof ArtifactChestContents c) {
				return ItemStack.listMatches(this.items, c.items);
			}

			return false;
		}
	}

	public int hashCode() {
		return this.hashCode;
	}

	public int getSlots() {
		return this.items.size();
	}

	static {
		CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(ArtifactChestContents::fromItems, e -> e.items);
		STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(ArtifactChestContents::new, (e) -> e.items);
	}

}
