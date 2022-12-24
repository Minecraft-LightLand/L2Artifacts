package dev.xkmc.l2artifacts.content.client.search.token;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;

import java.util.List;

public interface IArtifactFeature {

	MutableComponent getDesc();

	interface Sprite extends IArtifactFeature{

		ResourceLocation getIcon();
	}
	interface ItemIcon extends IArtifactFeature{

		Item getItemIcon();

	}

}
