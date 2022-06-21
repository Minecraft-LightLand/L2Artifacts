package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> {

	public ArtifactSlot() {
		super(ArtifactTypeRegistry.SLOT);
	}

	public void generate(ArtifactStats stat, RandomSource random) {
		List<ArtifactStatType> main_list = new ArrayList<>(SlotStatConfig.getInstance().available_main_stats.get(this));
		List<ArtifactStatType> sub_list = new ArrayList<>(SlotStatConfig.getInstance().available_sub_stats.get(this));

		ArtifactStatType main = main_list.get(random.nextInt(main_list.size()));
		sub_list.remove(main);
		stat.add(main, main.getInitialValue(stat.rank, random));
		int roll = stat.rank - 1;
		for (int i = 0; i < roll; i++) {
			if (sub_list.size() == 0) {
				break;
			}
			int index = random.nextInt(sub_list.size());
			ArtifactStatType sub = sub_list.get(index);
			sub_list.remove(index);
			stat.add(sub, sub.getSubValue(stat.rank, random));
		}
	}

}
