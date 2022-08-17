package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleCPSetEffect extends AbstractConditionalPersistentSetEffect<PeriodicData> {

	private final Predicate<Player> pred;
	private final Consumer<Player> cons;

	public SimpleCPSetEffect(LinearFuncEntry period, Predicate<Player> pred, Consumer<Player> cons) {
		super(period);
		this.pred = pred;
		this.cons = cons;
	}

	@Override
	protected boolean test(Player player, ArtifactSetConfig.Entry ent, int rank, PeriodicData data) {
		return pred.test(player);
	}

	@Override
	protected void perform(Player player, ArtifactSetConfig.Entry ent, int rank, PeriodicData data) {
		cons.accept(player);
	}

	@Override
	public PeriodicData getData(ArtifactSetConfig.Entry ent) {
		return new PeriodicData();
	}
}
