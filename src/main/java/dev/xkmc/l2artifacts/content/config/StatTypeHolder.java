package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public record StatTypeHolder(Holder<StatType> holder) implements IArtifactFeature.Sprite {

	@Override
	public ResourceLocation icon() {
		var override = holder.value().icon();
		if (override != null) return override;
		return getID().withPath(e -> "textures/stat_type/" + e + ".png");
	}

	@Override
	public MutableComponent getDesc() {
		return Component.translatable(getID().toLanguageKey("stat_type"));
	}


	public StatType value() {
		return holder.value();
	}

	public ResourceLocation getID() {
		return holder.unwrapKey().orElseThrow().location();
	}

}
