package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> {

	public ArtifactSlot() {
		super(ArtifactTypeRegistry.SLOT);
	}

	public void generate(ArtifactStats stat, Upgrade upgrade, RandomSource random) {
		List<ArtifactStatType> main_list = new ArrayList<>(SlotStatConfig.getInstance().available_main_stats.get(this));
		List<ArtifactStatType> sub_list = new ArrayList<>(SlotStatConfig.getInstance().available_sub_stats.get(this));

		ArtifactStatType main = main_list.get(random.nextInt(main_list.size()));
		sub_list.remove(main);
		stat.add(main, main.getInitialValue(stat.rank, random, upgrade.removeMain()));

		int roll = stat.rank - 1;
		for (int i = 0; i < roll; i++) {
			if (sub_list.size() == 0) {
				break;
			}
			ArtifactStatType sub;
			if (upgrade.stats.size() > 0) {
				sub = upgrade.stats.remove(0);
			} else {
				int index = random.nextInt(sub_list.size());
				sub = sub_list.get(index);
			}
			sub_list.remove(sub);
			stat.add(sub, sub.getSubValue(stat.rank, random, upgrade.removeSub()));
		}
	}

}
