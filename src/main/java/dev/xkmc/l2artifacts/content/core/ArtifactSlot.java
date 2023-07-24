package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> implements IArtifactFeature.Sprite {

	private final ArtifactSlotCuriosType curios;

	public ArtifactSlot(ArtifactSlotCuriosType curios) {
		super(ArtifactTypeRegistry.SLOT);
		this.curios = curios;
	}

	public void generate(ArtifactStats stat, Upgrade upgrade, RandomSource random) {
		List<ArtifactStatTypeHolder> main_list = new ArrayList<>(SlotStatConfig.getInstance().available_main_stats.get(this));
		List<ArtifactStatTypeHolder> sub_list = new ArrayList<>(SlotStatConfig.getInstance().available_sub_stats.get(this));

		ArtifactStatTypeHolder main = main_list.get(random.nextInt(main_list.size()));
		sub_list.remove(main);
		stat.add(main, main.get().getInitialValue(stat.rank, random, upgrade.removeMain()));

		int roll = stat.rank - 1;
		for (int i = 0; i < roll; i++) {
			if (sub_list.size() == 0) {
				break;
			}
			ArtifactStatTypeHolder sub;
			if (upgrade.stats.size() > 0) {
				sub = upgrade.stats.remove(0);
			} else {
				int index = random.nextInt(sub_list.size());
				sub = sub_list.get(index);
			}
			sub_list.remove(sub);
			stat.add(sub, sub.get().getSubValue(stat.rank, random, upgrade.removeSub()));
		}
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(CuriosApi.MODID, "textures/slot/empty_" + getRegistryName().getPath() + "_slot.png");
	}

	public String getCurioIdentifier() {
		return curios.getIdentifier();
	}
}
