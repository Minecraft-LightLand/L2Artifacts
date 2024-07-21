package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2complements.init.data.TagGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class ArtifactTagGen {

	public static TagKey<EntityType<?>> NO_DROP = TagKey.create(Registries.ENTITY_TYPE,
			new ResourceLocation(L2Artifacts.MODID, "no_drops"));

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(TagGen.SKILL_EFFECT)
				.add(ArtifactEffects.THERMAL_MOTIVE.get())
				.add(ArtifactEffects.FROST_SHIELD.get());
	}

	public static void onEntityTypeGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(NO_DROP)
				.addOptional(new ResourceLocation("mutantmonsters", "mutant_enderman"))
				.addOptional(new ResourceLocation("twilightforest", "death_tome"));
	}

}
