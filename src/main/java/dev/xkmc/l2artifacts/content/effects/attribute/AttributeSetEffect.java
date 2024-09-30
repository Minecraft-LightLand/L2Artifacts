package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public class AttributeSetEffect extends SetEffect {

	private final AttrSetEntry[] entries;

	public AttributeSetEffect(AttrSetEntry... entries) {
		super();
		this.entries = entries;
	}

	@Override
	public void update(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		for (AttrSetEntry entry : entries) {
			double val = entry.getValue(rank);
			AttributeInstance ins = player.getAttribute(entry.attr());
			if (ins == null)
				continue;
			var id = entry.getId(this);
			ins.removeModifier(id);
			if (enabled) {
				ins.addTransientModifier(new AttributeModifier(id, val, entry.op()));
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		for (AttrSetEntry ent : entries) {
			ans.add(ent.toComponent(rank));
		}
		return ans;
	}
}
