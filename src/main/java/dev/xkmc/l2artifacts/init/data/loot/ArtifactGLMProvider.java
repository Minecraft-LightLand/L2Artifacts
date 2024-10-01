package dev.xkmc.l2artifacts.init.data.loot;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.serial.loot.AddLootTableModifier;
import dev.xkmc.l2core.serial.loot.LootTableTemplate;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ArtifactGLMProvider extends GlobalLootModifierProvider {

	public ArtifactGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, L2Artifacts.MODID);
	}

	@Override
	protected void start() {
		this.add("health_based_1", new ArtifactLootModifier(100, 200, 1, ArtifactItems.RANDOM[0].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_2", new ArtifactLootModifier(200, 300, 1, ArtifactItems.RANDOM[1].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_3", new ArtifactLootModifier(300, 400, 1, ArtifactItems.RANDOM[2].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_4", new ArtifactLootModifier(400, 500, 1, ArtifactItems.RANDOM[3].asStack(), LootTableTemplate.byPlayer().build()));
		this.add("health_based_5", new ArtifactLootModifier(500, 0, 1, ArtifactItems.RANDOM[4].asStack(), LootTableTemplate.byPlayer().build()));

		add("fungus_infection", new AddLootTableModifier(ArtifactLootGen.DROP_FUNGUS.location(),
				DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType()).build(),
				LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity().effects(MobEffectsPredicate.Builder.effects()
								.and(ArtifactEffects.FUNGUS))).build()));

	}
}
