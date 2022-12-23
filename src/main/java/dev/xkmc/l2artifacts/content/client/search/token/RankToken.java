package dev.xkmc.l2artifacts.content.client.search.token;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Stream;

public record RankToken(int rank) implements IArtifactFeature.Sprite {

	public static final List<RankToken> ALL_RANKS = Stream.of(1, 2, 3, 4, 5).map(RankToken::new).toList();

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(L2Artifacts.MODID, "textures/item/rank_" + rank + ".png");
	}

}
