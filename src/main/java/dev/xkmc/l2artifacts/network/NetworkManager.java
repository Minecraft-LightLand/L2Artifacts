package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2serial.network.PacketHandler;

public class NetworkManager {

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			L2Artifacts.MODID, 1,
			e -> e.create(ChooseArtifactToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(SetFilterToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);

	public static void register() {
	}

}
