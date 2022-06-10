package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2library.network.BaseConfig;
import dev.xkmc.l2library.network.PacketHandlerWithConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.stream.Stream;

public class NetworkManager {

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			new ResourceLocation(L2Artifacts.MODID, "main"), 1, "artifact_config"
	);

	public static Stream<Map.Entry<String, BaseConfig>> getConfigs(String id) {
		return HANDLER.CONFIGS.entrySet().stream()
				.filter(e -> {
					String path = new ResourceLocation(e.getKey()).getPath();
					String[] paths = path.split("/");
					return paths[0].equals(id);
				});
	}

	public static BaseConfig getConfig(String id) {
		return HANDLER.CONFIGS.get(L2Artifacts.MODID + ":" + id);
	}

	public static void register() {
		HANDLER.addAfterReloadListener(() -> {
			SlotStatConfig.cache = null;
		});
	}

}
