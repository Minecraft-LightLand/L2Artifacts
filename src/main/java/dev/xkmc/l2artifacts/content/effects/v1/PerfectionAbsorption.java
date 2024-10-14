package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class PerfectionAbsorption extends AttributeSetEffect {

	private final LinearFuncEntry period, max;

	public PerfectionAbsorption(LinearFuncEntry period, LinearFuncEntry max) {
		super(new AttrSetEntry(Attributes.MAX_ABSORPTION,
				AttributeModifier.Operation.ADD_VALUE, max, false));
		this.period = period;
		this.max = max;
	}

	@Override
	public void tick(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled)
			return;
		int max = (int) this.max.getFromRank(rank);
		if (player.tickCount % period.getFromRank(rank) == 0) {
			if (player.getHealth() >= player.getMaxHealth()) {
				double current = player.getAbsorptionAmount();
				if (current < max) {
					player.setAbsorptionAmount((float) Math.min(max, current + 1));
				}
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		double period = this.period.getFromRank(rank) / 20;
		int max = (int) this.max.getFromRank(rank);
		return List.of(Component.translatable(getDescriptionId() + ".desc", period, max));
	}

}
