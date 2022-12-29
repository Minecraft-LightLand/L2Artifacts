package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;


public class AbyssAttackEffect extends SetEffect {


	private final LinearFuncEntry duration, level, hurt;

	public AbyssAttackEffect(LinearFuncEntry duration, LinearFuncEntry level, LinearFuncEntry hurt, int ids) {
		super(ids);
		this.duration = duration;
		this.level = level;
		this.hurt = hurt;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		event.getAttackTarget().addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (int) duration.getFromRank(rank), (int) level.getFromRank(rank)));
		event.getAttackTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, (int) duration.getFromRank(rank), (int) level.getFromRank(rank)));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		event.setAmount((float) (event.getAmount() * hurt.getFromRank(rank)));

	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int time = (int) Math.round(duration.getFromRank(rank) / 20);
		int level = (int) Math.round(this.level.getFromRank(rank) + 1);
		int hurt2 = (int) Math.round(this.hurt.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", time, level, hurt2));
	}

}
