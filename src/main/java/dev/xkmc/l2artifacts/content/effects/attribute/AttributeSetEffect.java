package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class AttributeSetEffect extends SetEffect {

	private final AttrSetEntry[] entries;

	public AttributeSetEffect(AttrSetEntry... entries) {
		super(entries.length);
		this.entries = entries;
	}

	@Override
	public void update(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			double val = entry.getValue(rank);
			AttributeInstance ins = player.getAttribute(entry.attr().get());
			if (ins == null)
				continue;
			ins.removeModifier(ent.id[i]);
			if (enabled) {
				ins.addTransientModifier(new AttributeModifier(ent.id[i], ent.getName(), val, entry.op()));
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		for (AttrSetEntry ent : entries) {
			double val = ent.getValue(rank);
			String sign = val > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
			ans.add(Component.translatable(
					sign + (ent.usePercent() ? 1 : 0),
					ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(ent.usePercent() ? val * 100 : val)),
					Component.translatable(ent.attr().get().getDescriptionId())));
		}
		return ans;
	}
}
