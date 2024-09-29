package dev.xkmc.l2artifacts.content.search.token;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IArtifactFeature {

	MutableComponent getDesc();

	@Nullable
	default NonNullList<ItemStack> getTooltipItems() {
		return null;
	}

	interface Sprite extends IArtifactFeature {

		ResourceLocation icon();
	}

	interface ItemIcon extends IArtifactFeature {

		Item getItemIcon();

	}

}
