package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public abstract class AbstractConditionalAttributeSetEffect<T extends AttributeSetData> extends PersistentDataSetEffect<T> {

	private final AttrSetEntry[] entries;

	public AbstractConditionalAttributeSetEffect(AttrSetEntry... entries) {
		super(entries.length);
		this.entries = entries;
	}

	protected void addAttributes(Player player, ArtifactSetConfig.Entry ent, int rank, T data) {
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			AttributeInstance ins = player.getAttribute(entry.attr().get());
			if (ins == null) continue;
			UUID id = ent.id[i];
			if (ins.getModifier(id) != null) continue;
			double val = entry.getValue(rank);
			ins.addTransientModifier(new AttributeModifier(id, ent.getName(), val, entry.op()));
		}
	}

	protected abstract T getData();

	@Override
	public T getData(ArtifactSetConfig.Entry ent) {
		T ans = getData();
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			UUID id = ent.id[i];
			ans.list.add(new AttributeSetData.AttributePair(entry.attr().get(), id));
		}
		return ans;
	}

	protected MutableComponent getConditionText(int rank) {
		return Component.translatable(getDescriptionId() + ".desc");
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		ans.add(getConditionText(rank));
		for (AttrSetEntry ent : entries) {
			double val = ent.getValue(rank);
			String sign = val > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
			ans.add(MutableComponent.create(new TranslatableContents(
					sign + (ent.usePercent() ? 1 : 0),
					ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(ent.usePercent() ? val * 100 : val)),
					MutableComponent.create(new TranslatableContents(ent.attr().get().getDescriptionId())))));
		}
		return ans;
	}
}
