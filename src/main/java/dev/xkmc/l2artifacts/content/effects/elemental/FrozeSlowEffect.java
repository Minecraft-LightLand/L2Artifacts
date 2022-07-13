package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class FrozeSlowEffect extends SetEffect {

	private final LinearFuncEntry factor, period, level;

	public FrozeSlowEffect(LinearFuncEntry factor, LinearFuncEntry period, LinearFuncEntry level) {
		super(0);
		this.factor = factor;
		this.period = period;
		this.level = level;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (player.isOnFire()) return;
		EffectUtil.addEffect(event.getAttackTarget(), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
				(int) period.getFromRank(rank), (int) level.getFromRank(rank)), EffectUtil.AddReason.NONE, player);
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		double dmg = (this.factor.getFromRank(item.rank) - 1) * 100;
		double period = this.period.getFromRank(item.rank) / 20;
		Component level = Component.translatable("potion.potency." + this.level.getFromRank(item.rank));
		return List.of(Component.translatable(getDescriptionId() + ".desc", dmg, level, period));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		if (event.getSource().isFire()) {
			event.setAmount((float) (event.getAmount() * factor.getFromRank(rank)));
		}
	}
}
