package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> implements IArtifactFeature.Sprite {

	private final ArtifactSlotCuriosType curios;

	public ArtifactSlot(ArtifactSlotCuriosType curios) {
		super(ArtifactTypeRegistry.SLOT);
		this.curios = curios;
	}

	public void generate(ArtifactStats stat, Upgrade upgrade, RandomSource random) {
		var main_list = new ArrayList<>(SlotStatConfig.getInstance().available_main_stats.get(this));
		var sub_list = new ArrayList<>(SlotStatConfig.getInstance().available_sub_stats.get(this));

		var main = main_list.get(random.nextInt(main_list.size()));
		sub_list.remove(main);
		stat.add(main, main.value().getInitialValue(random, upgrade.removeMain()));

		int roll = stat.rank - 1;
		for (int i = 0; i < roll; i++) {
			if (sub_list.isEmpty()) {
				break;
			}
			Holder<StatType> sub;
			if (!upgrade.stats.isEmpty()) {
				sub = upgrade.stats.removeFirst();
			} else {
				int index = random.nextInt(sub_list.size());
				sub = sub_list.get(index);
			}
			sub_list.remove(sub);
			stat.add(sub, sub.value().getSubValue(random, upgrade.removeSub()));
		}
	}

	@Override
	public ResourceLocation icon() {
		return getRegistryName().withPath(e -> "textures/slot/empty_artifact_" + e + "_slot.png");
	}

	public String getCurioIdentifier() {
		return curios.getIdentifier();
	}
}
