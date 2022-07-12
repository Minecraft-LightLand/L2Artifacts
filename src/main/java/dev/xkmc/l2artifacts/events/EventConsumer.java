package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.world.entity.player.Player;

public interface EventConsumer<T> {

	void apply(SetEffect set, Player player, ArtifactSetConfig.Entry ent, int rank, T event);

}
