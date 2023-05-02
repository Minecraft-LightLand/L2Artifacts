package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class ExecutorLimitEffect extends SetEffect {

	private final LinearFuncEntry factor;

	public ExecutorLimitEffect(LinearFuncEntry factor) {
		super(0);
		this.factor = factor;
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {
		if (event.getSource().getEntity() == player && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			event.setAmount((float) (Math.min(player.getMaxHealth(), event.getAmount()) * factor.getFromRank(rank)));
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double amount = this.factor.getFromRank(rank) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(amount)));
	}

}
