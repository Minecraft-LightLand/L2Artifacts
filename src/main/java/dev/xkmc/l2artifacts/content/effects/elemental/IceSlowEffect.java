package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class IceSlowEffect extends SetEffect {

	public static final double FACTOR = 1.2;//TODO config
	public static final int TIME = 200; // TODO config

	public IceSlowEffect() {
		super(0);
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (player.isOnFire()) return;
		EffectUtil.addEffect(event.getAttackTarget(), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, TIME, rank), EffectUtil.AddReason.NONE, player);
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		if (event.getSource().isFire()) {
			event.setAmount((float) (event.getAmount() * FACTOR));
		}
	}
}
