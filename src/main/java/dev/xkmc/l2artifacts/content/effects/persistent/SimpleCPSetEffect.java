package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class SimpleCPSetEffect extends AbstractConditionalPersistentSetEffect<PeriodicData> {

	private final Predicate<Player> pred;
	private final BiConsumer<Player, Integer> cons;
	private final BiFunction<Integer, String, MutableComponent> desc;

	public SimpleCPSetEffect(LinearFuncEntry period, Predicate<Player> pred, BiConsumer<Player, Integer> cons, BiFunction<Integer, String, MutableComponent> desc) {
		super(period);
		this.pred = pred;
		this.cons = cons;
		this.desc = desc;
	}

	@Override
	protected boolean test(Player player, ArtifactSetConfig.Entry ent, int rank, PeriodicData data) {
		return pred.test(player);
	}

	@Override
	protected void perform(Player player, ArtifactSetConfig.Entry ent, int rank, PeriodicData data) {
		cons.accept(player, rank);
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		return List.of(desc.apply(rank, getDescriptionId() + ".desc"));
	}

	@Override
	public PeriodicData getData(ArtifactSetConfig.Entry ent) {
		return new PeriodicData();
	}
}
