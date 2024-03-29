package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

import java.util.List;

public class DeadCellParry extends SetEffect {

	private final LinearFuncEntry reflect;

	public DeadCellParry(LinearFuncEntry reflect) {
		super(0);
		this.reflect = reflect;
	}

	@Override
	public void playerShieldBlock(Player player, ArtifactSetConfig.Entry entry, int rank, ShieldBlockEvent event) {
		if (event.getDamageSource().is(L2DamageTypes.DIRECT) && event.getDamageSource().getDirectEntity() instanceof LivingEntity le) {
			float dmg = (float) reflect.getFromRank(rank) * event.getBlockedDamage();
			GeneralEventHandler.schedule(() -> le.hurt(player.damageSources().thorns(player), dmg));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int val = (int) Math.round(reflect.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", val));
	}
}
