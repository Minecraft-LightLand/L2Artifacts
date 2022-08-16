package dev.xkmc.l2artifacts.content.capability;

import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import java.util.HashMap;

@SerialClass
public class ArtifactData extends PlayerCapabilityTemplate<ArtifactData> {

	public static final Capability<ArtifactData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<ArtifactData> HOLDER = new PlayerCapabilityHolder<>(
			new ResourceLocation(L2Artifacts.MODID, "artifact_data"), CAPABILITY,
			ArtifactData.class, ArtifactData::new, PlayerCapabilityNetworkHandler::new);

	public static void register() {
	}

	@SerialClass.SerialField
	public HashMap<SetEffect, SetEffectData> data = new HashMap<>();

	public <T extends SetEffectData> T getOrCreateData(PersistentDataSetEffect<T> setEffect) {
		return Wrappers.cast(data.computeIfAbsent(setEffect, e -> setEffect.getData()));
	}

	@Nullable
	public <T extends SetEffectData> T getData(PersistentDataSetEffect<T> setEffect) {
		return Wrappers.cast(data.get(setEffect));
	}

	@Override
	public void tick() {
		data.entrySet().removeIf(e -> e.getValue().tick());
	}

	public boolean hasData(PersistentDataSetEffect<?> eff) {
		return data.containsKey(eff);
	}
}
