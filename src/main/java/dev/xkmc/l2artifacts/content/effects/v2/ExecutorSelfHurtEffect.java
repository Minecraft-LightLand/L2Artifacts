package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.damage.DamageTypeRoot;
import dev.xkmc.l2library.init.events.damage.DefaultDamageState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

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
	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		double damage = event.getEntity().getMaxHealth() * factor.getFromRank(rank);
		var type = DamageTypeRoot.of(DamageTypes.PLAYER_ATTACK)
				.enable(DefaultDamageState.BYPASS_ARMOR)
				.enable(DefaultDamageState.BYPASS_MAGIC)
				.getHolder(player.level);
		player.hurt(new DamageSource(type, player), (float) damage);
	}
}
