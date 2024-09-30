package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Stream;

public record RankToken(int rank) implements IArtifactFeature.Sprite {

	public static final List<RankToken> ALL_RANKS = Stream.of(1, 2, 3, 4, 5).map(RankToken::new).toList();

	@Override
	public ResourceLocation icon() {
		return L2Artifacts.loc("textures/rank/" + rank + ".png");
	}

	@Override
	public MutableComponent getDesc() {
		return ArtifactLang.getTranslate("tooltip.rank." + rank);
	}
}
