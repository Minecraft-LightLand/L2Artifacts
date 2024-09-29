package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractConditionalPersistentSetEffect<T extends PeriodicData> extends PersistentDataSetEffect<T> {

	private final LinearFuncEntry period;

	public AbstractConditionalPersistentSetEffect(LinearFuncEntry period) {
		super(0);
		this.period = period;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		T data = fetch(player, ent);
		data.update(2, rank);
		if (!test(player, ent, rank, data)) {
			data.tick_count = 0;
		} else {
			data.tick_count++;
			if (data.tick_count >= period.getFromRank(rank)) {
				data.tick_count -= period.getFromRank(rank);
				perform(player, ent, rank, data);
			}
		}
	}

	protected abstract boolean test(Player player, ArtifactSetConfig.Entry ent, int rank, T data);

	protected abstract void perform(Player player, ArtifactSetConfig.Entry ent, int rank, T data);

	@Override
	public abstract T getData(ArtifactSetConfig.Entry ent);

}
