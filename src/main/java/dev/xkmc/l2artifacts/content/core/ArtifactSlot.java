package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.search.filter.IArtifactFeature;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.resources.ResourceLocation;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> implements IArtifactFeature.Sprite {

	private final ArtifactSlotCuriosType curios;

	public ArtifactSlot(ArtifactSlotCuriosType curios) {
		super(ArtifactTypeRegistry.SLOT);
		this.curios = curios;
	}

	@Override
	public ResourceLocation icon() {
		return getRegistryName().withPath(e -> "textures/slot/empty_artifact_" + e + "_slot.png");
	}

	public String getCurioIdentifier() {
		return curios.getIdentifier();
	}
}
