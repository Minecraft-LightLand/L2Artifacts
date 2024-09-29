package dev.xkmc.l2artifacts.content.effects.v4;


import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2artifacts.init.registrate.items.LAItem4;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public class LongShooterEffect extends AbstractConditionalAttributeSetEffect<AttributeSetData> {

	public LongShooterEffect(AttrSetEntry... entries) {
		super(entries);

	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		if (player.tickCount % 10 != 0) return;
		boolean enhanced = LAItem4.EFF_LONGSHOOTER_4.get().fetchNullable(player) != null;
		double r = enhanced ? 6 : 8;
		var box = AABB.ofSize(player.getPosition(0), r, r, r);
		if (player.level().getEntities(EntityTypeTest.forClass(Monster.class), box, EntitySelector.NO_SPECTATORS).isEmpty()) {
			var data = fetch(player, ent);
			data.update(11, rank);
			addAttributes(player, ent, rank, data);
		} else {
			if (fetchNullable(player) != null && !player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1));
			}
		}
	}


	@Override
	public AttributeSetData getData() {
		return new AttributeSetData();
	}

	protected MutableComponent getConditionText(int rank) {

		return Component.translatable(getDescriptionId() + ".desc");
	}
}
