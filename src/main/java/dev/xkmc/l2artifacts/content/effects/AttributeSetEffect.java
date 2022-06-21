package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class AttributeSetEffect extends SetEffect {

	public record AttrSetEntry(Supplier<Attribute> attr, AttributeModifier.Operation op,
							   double base, double slope, boolean usePercent) {

	}

	private final AttrSetEntry[] entries;

	public AttributeSetEffect(AttrSetEntry... entries) {
		super(entries.length);
		this.entries = entries;
	}

	@Override
	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			double val = entry.base + entry.slope * (rank - 1);
			AttributeInstance ins = player.getAttribute(entry.attr().get());
			if (ins == null)
				continue;
			ins.removeModifier(ent.id[i]);
			if (enabled) {
				ins.addTransientModifier(new AttributeModifier(ent.id[i], ent.str, val, entry.op));
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		List<MutableComponent> ans = new ArrayList<>();
		for (AttrSetEntry ent : entries) {
			double val = ent.base + ent.slope * (item.rank - 1);
			String sign = val > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
			ans.add(MutableComponent.create(new TranslatableContents(
					sign + (ent.usePercent ? 1 : 0),
					ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(ent.usePercent ? val * 100 : val)),
					MutableComponent.create(new TranslatableContents(ent.attr.get().getDescriptionId())))));
		}
		return ans;
	}
}
