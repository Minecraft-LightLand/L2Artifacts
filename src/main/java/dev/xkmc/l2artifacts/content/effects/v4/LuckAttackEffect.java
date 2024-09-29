package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class LuckAttackEffect extends AbstractConditionalAttributeSetEffect<LuckAttackData> {

	private final LinearFuncEntry duration, count;

	public LuckAttackEffect(LinearFuncEntry duration, LinearFuncEntry count, AttrSetEntry... entries) {
		super(entries);
		this.duration = duration;
		this.count = count;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		LuckAttackData data = ConditionalData.HOLDER.get(player).getData(getKey());
		if (data != null && data.count == count.getFromRank(rank))
			addAttributes(player, ent, rank, data);
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		LuckAttackData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
		data.update((int) duration.getFromRank(rank), rank);
		data.count++;

	}

	@Override
	protected MutableComponent getConditionText(int rank) {
		int c = (int) Math.round(count.getFromRank(rank));
		double t = duration.getFromRank(rank) / 20d;
		return Component.translatable(getDescriptionId() + ".desc", c, t);
	}


	@Override
	protected LuckAttackData getData() {
		return new LuckAttackData();
	}
}
