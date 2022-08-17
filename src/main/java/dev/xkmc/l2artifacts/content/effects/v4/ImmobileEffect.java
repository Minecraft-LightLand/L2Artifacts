package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class ImmobileEffect extends PersistentDataSetEffect<ImmobileData> {

	private static final double TOLERANCE = 1e-3;

	private final LinearFuncHandle protection, threshold;

	public ImmobileEffect(LinearFuncHandle protection, LinearFuncHandle threshold) {
		super(0);
		this.protection = protection;
		this.threshold = threshold;
	}


	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		ImmobileData data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
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
		int prot = (int) Math.round(protection.getValue(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", prot));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		ImmobileData data = ArtifactData.HOLDER.get(player).getData(this);
		if (data == null) return;
		if (data.time >= threshold.getValue(rank)) {
			event.setAmount((float) (event.getAmount() * protection.getValue(rank)));
		}
	}

	@Override
	public ImmobileData getData(ArtifactSetConfig.Entry ent) {
		return new ImmobileData();
	}

}
