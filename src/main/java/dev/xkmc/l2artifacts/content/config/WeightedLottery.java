package dev.xkmc.l2artifacts.content.config;

import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;

import java.util.LinkedList;
import java.util.List;

public class WeightedLottery {

	private final List<Pair<Holder<StatType>, Integer>> list = new LinkedList<>();
	private final RandomSource source;
	private int total = 0;

	public WeightedLottery(RegistryAccess access, RandomSource source, boolean main) {
		this.source = source;
		ArtifactTypeRegistry.STAT_TYPE.getAll(access)
				.map(e -> Pair.of(e, main ? e.value().mainWeight() : e.value().subWeight()))
				.filter(e -> e.second() > 0)
				.forEach(list::add);
		for (var e : list) {
			total += e.second();
		}
	}

	public Holder<StatType> poll() {
		var itr = list.iterator();
		int sel = source.nextInt(total);
		while (true) {
			var e = itr.next();
			sel -= e.second();
			if (sel < 0 || !itr.hasNext()) {
				itr.remove();
				total -= e.second();
				return e.first();
			}
		}
	}

	public boolean isEmpty() {
		return total == 0 || list.isEmpty();
	}

	public void remove(Holder<StatType> sub) {
		var itr = list.iterator();
		while (itr.hasNext()) {
			var e = itr.next();
			if (e.equals(sub)) {
				total -= e.second();
				itr.remove();
				return;
			}
		}
	}

}
