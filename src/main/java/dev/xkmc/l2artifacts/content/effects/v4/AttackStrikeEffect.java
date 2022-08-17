package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.entity.player.Player;

public class AttackStrikeEffect extends AbstractConditionalAttributeSetEffect<AttackStrikeData> {

	private final LinearFuncEntry duration, count;

	public AttackStrikeEffect(LinearFuncEntry duration, LinearFuncEntry count) {
		this.duration = duration;
		this.count = count;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		AttackStrikeData data = ArtifactData.HOLDER.get(player).getData(this);
		if (data != null && data.count >= count.getFromRank(rank))
			addAttributes(player, ent, rank, data); // efficient operation, perform every tick
	}

	@Override
	protected void tickData(Player player, ArtifactSetConfig.Entry ent, int rank, AttackStrikeData data) {
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getStrength() > 0.99) {
			AttackStrikeData data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
			data.update((int) duration.getFromRank(rank), rank);
			data.count++;
		}
	}

	@Override
	protected AttackStrikeData getData() {
		return new AttackStrikeData();
	}

}
