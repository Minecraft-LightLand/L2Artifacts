package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2complements.init.data.TagGen;
import net.minecraft.world.effect.MobEffect;

public class ArtifactTagGen {

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(TagGen.SKILL_EFFECT)
				.add(ArtifactEffects.THERMAL_MOTIVE.get())
				.add(ArtifactEffects.FROST_SHIELD.get());
	}

}
