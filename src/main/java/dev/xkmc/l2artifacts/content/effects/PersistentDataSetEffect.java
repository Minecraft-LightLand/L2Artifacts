package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.capability.SetEffectData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import dev.xkmc.l2library.capability.conditionals.TokenKey;
import dev.xkmc.l2library.capability.conditionals.TokenProvider;
import net.minecraft.world.entity.player.Player;

public abstract class PersistentDataSetEffect<T extends SetEffectData> extends SetEffect implements TokenProvider<T, ArtifactSetConfig.Entry> {

	public PersistentDataSetEffect(int n) {
		super(n);
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		T data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
		data.update(2, rank);
	}

	public abstract T getData(ArtifactSetConfig.Entry ent);

	@Override
	public TokenKey<T> getKey() {
		return new TokenKey<>(L2Artifacts.MODID, getRegistryName().toString());
	}

}
