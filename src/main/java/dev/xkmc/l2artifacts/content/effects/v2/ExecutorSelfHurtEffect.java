package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
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
		player.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic(), (float) damage);
	}
}
