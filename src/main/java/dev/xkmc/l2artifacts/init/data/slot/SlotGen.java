package dev.xkmc.l2artifacts.init.data.slot;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class SlotGen extends CuriosDataProvider {

	public SlotGen(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(modId, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider provider, ExistingFileHelper existingFileHelper) {
		for (var e : ArtifactSlotCuriosType.values()) {
			createSlot(e.getIdentifier()).order(e.priority).icon(L2Artifacts.loc("slot/empty_" + e.getIdentifier() + "_slot"));
		}
		createEntities("l2artifact_player")
				.addEntities(EntityType.PLAYER)
				.addSlots(Stream.of(ArtifactSlotCuriosType.values())
						.map(ArtifactSlotCuriosType::getIdentifier).toArray(String[]::new));
	}

}
