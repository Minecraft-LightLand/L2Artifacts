package dev.xkmc.l2artifacts.init.registrate.entries;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.resources.ResourceLocation;

public class SetRegHelper {

	private final ArtifactRegistrate reg;
	private final String id;

	public SetRegHelper(ArtifactRegistrate reg, String id) {
		this.reg = reg;
		this.id = id;
	}

	public ResourceLocation getId() {
		return new ResourceLocation(reg.getModid(), id);
	}

	public <I extends BaseArtifact> LinearFuncEntry regLinear(String id, double v, double s) {
		return reg.regLinear(id, this, v, s);
	}

	public SetBuilder<ArtifactSet, BaseArtifact, ArtifactRegistrate> regSet(int min, int max, String lang) {
		return reg.regSet(id, ArtifactSet::new, min, max, lang);
	}

	public <T extends SetEffect> SetEffectBuilder<T, ArtifactRegistrate> setEffect(String id, NonNullSupplier<T> sup) {
		return reg.setEffect(id, sup);
	}
}
