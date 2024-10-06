package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.search.filter.ArtifactFilterData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record FilterDataToServer(ArtifactFilterData data) implements SerialPacketBase<FilterDataToServer> {

	@Override
	public void handle(Player ctx) {
		ArtifactTypeRegistry.FILTER.type().getOrCreate(ctx).setFilter(data);
	}

}
