package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.capability.conditionals.TokenKey;
import dev.xkmc.l2core.init.L2LibReg;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class PersistentDataSetEffect<T extends SetEffectData> extends PlayerOnlySetEffect {

	public PersistentDataSetEffect(int n) {
		super(n);
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		fetch(player, ent).update(2, rank);
	}

	public @Nullable T fetchNullable(Player player) {
		return L2LibReg.CONDITIONAL.type().getExisting(player).map(e -> e.getData(getKey())).orElse(null);
	}

	public T fetch(Player player, ArtifactSetConfig.Entry ent) {
		return L2LibReg.CONDITIONAL.type().getOrCreate(player).getOrCreateData(getKey(), () -> getData(ent));
	}

	public abstract T getData(ArtifactSetConfig.Entry ent);

	public TokenKey<T> getKey() {
		return new TokenKey<>(L2Artifacts.MODID, getRegistryName().toString());
	}

}
