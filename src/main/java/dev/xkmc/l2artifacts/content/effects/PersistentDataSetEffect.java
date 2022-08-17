package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.capability.SetEffectData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import net.minecraft.world.entity.player.Player;

public abstract class PersistentDataSetEffect<T extends SetEffectData> extends SetEffect {

	public PersistentDataSetEffect(int n) {
		super(n);
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		T data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
		tickData(player, ent, rank, data);
	}

	protected void tickData(Player player, ArtifactSetConfig.Entry ent, int rank, T data) {
		data.update(2, rank);
	}

	public abstract T getData(ArtifactSetConfig.Entry ent);

}
