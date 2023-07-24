package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2library.base.datapack.EntryHolder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record ArtifactStatTypeHolder(Holder.Reference<ArtifactStatType> holder)
		implements EntryHolder<ArtifactStatType>, IArtifactFeature.Sprite {

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(getID().getNamespace(), "textures/stat_type/" + getID().getPath() + ".png");
	}

	@Override
	public MutableComponent getDesc() {
		return EntryHolder.super.getDesc();
	}

}
