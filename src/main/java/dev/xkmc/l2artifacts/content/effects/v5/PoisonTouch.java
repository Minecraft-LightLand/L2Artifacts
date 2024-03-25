package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.mobeffects.EffectDesc;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.stream.Stream;

public class PoisonTouch extends SetEffect {

	private static final List<MobEffect> LIST = List.of(MobEffects.POISON, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS);

	private final LinearFuncEntry chance, duration;

	public PoisonTouch(LinearFuncEntry chance, LinearFuncEntry duration) {
		super(0);
		this.chance = chance;
		this.duration = duration;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		for (var e : LIST) {
			if (player.getRandom().nextDouble() < chance.getFromRank(rank)) {
				event.getAttackTarget().addEffect(new MobEffectInstance(e, (int) duration.getFromRank(rank)), player);
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		return List.of(Component.translatable(getDescriptionId() + ".desc", Stream.concat(
				Stream.of(Component.literal("" + (int) Math.round(chance.getFromRank(rank) * 100))),
				LIST.stream().map(e -> EffectDesc.getDesc(new MobEffectInstance(e,
						(int) duration.getFromRank(rank)), true))).toArray()));
	}

}
