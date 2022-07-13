package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
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
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		var ans = super.getDetailedDescription(item);
		ans.add(Component.translatable(getDescriptionId() + ".desc", factor.getFromRank(item.rank) * 100));
		return ans;
	}

	@Override
	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		double damage = event.getEntity().getMaxHealth() * factor.getFromRank(rank);
		player.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic(), (float) damage);
	}
}
