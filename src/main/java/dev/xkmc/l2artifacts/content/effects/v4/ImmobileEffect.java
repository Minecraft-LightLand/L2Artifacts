package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ImmobileEffect extends PersistentDataSetEffect<ImmobileData> {

	private static final double TOLERANCE = 1e-3;

	private final LinearFuncEntry protection, threshold;

	public ImmobileEffect(LinearFuncEntry protection, LinearFuncEntry threshold) {
		super(0);
		this.protection = protection;
		this.threshold = threshold;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		ImmobileData data = fetch(player, ent);
		data.update(2, rank);
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		if (new Vec3(x, y, z).distanceTo(new Vec3(data.x, data.y, data.z)) < TOLERANCE) {
			data.time++;
		} else {
			data.time = 0;
			data.x = x;
			data.y = y;
			data.z = z;
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int prot = (int) Math.round(protection.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", threshold.getFromRank(rank) / 20d, prot));
	}

	@Override
	public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
		ImmobileData data = fetchNullable(player);
		if (data == null) return;
		if (data.time >= threshold.getFromRank(rank)) {
			cache.addDealtModifier(DamageModifier.multTotal((float) protection.getFromRank(rank), getRegistryName()));
		}
	}

	@Override
	public ImmobileData getData(ArtifactSetConfig.Entry ent) {
		return new ImmobileData();
	}

}
