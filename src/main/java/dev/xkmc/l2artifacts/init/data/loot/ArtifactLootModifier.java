package dev.xkmc.l2artifacts.init.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ArtifactLootModifier extends LootModifier {

	public static final Codec<ArtifactLootModifier> CODEC = RecordCodecBuilder.create(i ->
			codecStart(i).apply(i, ArtifactLootModifier::new));

	public ArtifactLootModifier(LootItemCondition... conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (!context.hasParam(LootContextParams.THIS_ENTITY)) return list;
		Entity entity = context.getParam(LootContextParams.THIS_ENTITY);
		if (entity instanceof LivingEntity le && entity instanceof Enemy) {
			float health = le.getMaxHealth();
			int rank = (int) Math.floor(health / 100);
			if (rank > 5) rank = 5;
			if (rank > 0) {
				list.add(ArtifactItemRegistry.RANDOM[rank - 1].asStack());
			}
		}
		return list;
	}

	@Override
	public Codec<ArtifactLootModifier> codec() {
		return CODEC;
	}
}
