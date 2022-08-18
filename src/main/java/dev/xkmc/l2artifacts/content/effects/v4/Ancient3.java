package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.entity.player.Player;

public class Ancient3 extends AbstractConditionalAttributeSetEffect<AncientData3> {
	private final LinearFuncEntry duration, count;

	public Ancient3(LinearFuncEntry duration, LinearFuncEntry count, AttrSetEntry...entries) {
		super(entries);
		this.duration = duration;
		this.count = count;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		AncientData3 data = ArtifactData.HOLDER.get(player).getOrCreateData(this,ent);
		data.update(3, rank);
		if (data != null && data.count >= count.getFromRank(rank))
			addAttributes(player, ent, rank, data); // efficient operation, perform every tick
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (event.getStrength() > 0.99) {
			AncientData3 data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
			data.update((int) duration.getFromRank(rank), rank);
			data.count++;
		}
	}

	@Override
	protected AncientData3 getData() {
		return new AncientData3();
	}

}
