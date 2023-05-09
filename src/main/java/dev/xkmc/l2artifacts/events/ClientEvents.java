package dev.xkmc.l2artifacts.events;

import com.mojang.blaze3d.platform.InputConstants;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapOverlay;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.Keys;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2artifacts.network.SetSelectedToServer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Artifacts.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

	@SubscribeEvent
	public static void keyEvent(InputEvent.Key event) {
		if (ArtifactSwapOverlay.INSTANCE.isScreenOn()) {
			if (event.getKey() == Keys.SWAP.map.getKey().getValue() && event.getAction() == InputConstants.PRESS) {
				NetworkManager.HANDLER.toServer(new SetSelectedToServer(SetSelectedToServer.SWAP));
			} else if (event.getKey() == Keys.UP.map.getKey().getValue() && event.getAction() == InputConstants.PRESS) {
				NetworkManager.HANDLER.toServer(new SetSelectedToServer(SetSelectedToServer.UP));
			} else if (event.getKey() == Keys.DOWN.map.getKey().getValue() && event.getAction() == InputConstants.PRESS) {
				NetworkManager.HANDLER.toServer(new SetSelectedToServer(SetSelectedToServer.DOWN));
			} else if (Minecraft.getInstance().options.keyShift.isDown()) {
				for (int i = 0; i < 9; i++) {
					if (Minecraft.getInstance().options.keyHotbarSlots[i].consumeClick()) {
						NetworkManager.HANDLER.toServer(new SetSelectedToServer(i));
					}
				}
			}
		}
	}

}
