package dev.xkmc.l2artifacts.content.client.search.token;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface IArtifactFeature {

	interface Sprite extends IArtifactFeature{

		ResourceLocation getIcon();
	}
	interface ItemIcon extends IArtifactFeature{

		Item getItemIcon();

	}

}
