package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public class ExecutorSelfHurtEffect extends AttributeSetEffect {

	private final LinearFuncEntry factor;

	public ExecutorSelfHurtEffect(AttrSetEntry entry, LinearFuncEntry factor) {
		super(entry);
		this.factor = factor;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		var ans = super.getDetailedDescription(rank);
		double val = factor.getFromRank(rank) * 100;
		ans.add(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(val)));
		return ans;
	}

	@Override
	public void playerKillOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		double damage = event.getEntity().getMaxHealth() * factor.getFromRank(rank);
		var type = DamageTypeRoot.of(DamageTypes.PLAYER_ATTACK)
				.enable(DefaultDamageState.BYPASS_ARMOR)
				.enable(DefaultDamageState.BYPASS_MAGIC)
				.getHolder(le.level());
		le.hurt(new DamageSource(type, le), (float) damage);
	}
}
