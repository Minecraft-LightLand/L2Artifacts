package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class WrathEffect extends SetEffect {

	private final Predicate<LivingEntity> pred;
	private final LinearFuncEntry dec, inc;

	public WrathEffect(Predicate<LivingEntity> pred, LinearFuncEntry dec, LinearFuncEntry inc) {
		super(0);
		this.pred = pred;
		this.dec = dec;
		this.inc = inc;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		boolean bool = pred.test(event.getAttackTarget());
		double factor = bool ? inc.getFromRank(rank) : dec.getFromRank(rank);
		event.setDamageModified((float) (event.getDamageModified() * factor));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		double inc = this.inc.getFromRank(item.rank) * 100;
		double dec = this.dec.getFromRank(item.rank) * 100;
		return List.of(Component.translatable(getDescriptionId() + ".desc", (int) Math.round(inc), (int) Math.round(dec)));
	}

}
