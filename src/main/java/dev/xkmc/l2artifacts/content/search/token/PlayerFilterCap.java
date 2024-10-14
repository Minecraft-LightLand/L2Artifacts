package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.search.filter.ArtifactFilterData;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class PlayerFilterCap extends PlayerCapabilityTemplate<PlayerFilterCap> {

	@SerialField
	private ArtifactFilterData data = new ArtifactFilterData();

	public void syncToServer(Player player) {
		L2Artifacts.HANDLER.toServer(new FilterDataToServer(data));
	}

	public ArtifactFilterData getFilter() {
		return data;
	}

	public void setFilter(ArtifactFilterData data) {
		this.data = data;
	}

	public void initFilter(ServerPlayer player) {
		data.initFilter();
		ArtifactTypeRegistry.FILTER.type().network.toClient(player);
	}

}
