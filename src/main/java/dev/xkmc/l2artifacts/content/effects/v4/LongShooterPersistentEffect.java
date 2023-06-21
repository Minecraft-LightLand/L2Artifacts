package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class LongShooterPersistentEffect extends AbstractConditionalAttributeSetEffect<LongShooterPersistentData> {

	public LongShooterPersistentEffect(AttrSetEntry... entries) {
		super(entries);

	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		if (player.tickCount % 10 == 0) {
			if (player.level().getEntities(EntityTypeTest.forClass(Monster.class), new AABB(player.getPosition(0), player.getPosition(0)).inflate(6), EntitySelector.NO_SPECTATORS).isEmpty()) {
				LongShooterPersistentData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
				data.update(11, rank);
				addAttributes(player, ent, rank, data);
				data.old = true;
			} else if
			(!player.level().getEntities(EntityTypeTest.forClass(Monster.class), new AABB(player.getPosition(0), player.getPosition(0)).inflate(6), EntitySelector.NO_SPECTATORS).isEmpty()) {
				LongShooterPersistentData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
				if (data.old) {
					data.update(40, rank);
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1));
					data.old = false;
				}
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		ans.add(getConditionText(rank));
		return ans;
	}

	@Override
	public LongShooterPersistentData getData() {
		return new LongShooterPersistentData();
	}


	protected MutableComponent getConditionText(int rank) {

		return Component.translatable(getDescriptionId() + ".desc");
	}
}
