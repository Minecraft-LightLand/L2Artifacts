package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2complements.init.data.LCTagGen;
import dev.xkmc.l2core.init.L2TagGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModList;

public class ArtifactTagGen {

	public static TagKey<EntityType<?>> NO_DROP = TagKey.create(Registries.ENTITY_TYPE, L2Artifacts.loc("no_drops"));

	public static void onEffectTagGen(RegistrateTagsProvider.IntrinsicImpl<MobEffect> pvd) {
		pvd.addTag(L2TagGen.TRACKED_EFFECTS)
				.add(ArtifactEffects.FUNGUS.get())
				.add(ArtifactEffects.FLESH_OVERGROWTH.get());
		if (ModList.get().isLoaded(L2Complements.MODID)) {
			pvd.addTag(LCTagGen.SKILL_EFFECT)
					.add(ArtifactEffects.THERMAL_MOTIVE.get())
					.add(ArtifactEffects.FROST_SHIELD.get());
		}
	}

	public static void onEntityTypeGen(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> pvd) {
		pvd.addTag(NO_DROP)
				.addOptional(ResourceLocation.fromNamespaceAndPath("mutantmonsters", "mutant_enderman"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("twilightforest", "death_tome"));
	}

}
