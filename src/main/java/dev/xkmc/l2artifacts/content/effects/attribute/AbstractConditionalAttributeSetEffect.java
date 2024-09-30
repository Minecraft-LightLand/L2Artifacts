package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConditionalAttributeSetEffect<T extends AttributeSetData> extends PersistentDataSetEffect<T> {

	private final AttrSetEntry[] entries;

	public AbstractConditionalAttributeSetEffect(AttrSetEntry... entries) {
		this.entries = entries;
	}

	protected void addAttributes(Player player, int rank) {
		for (AttrSetEntry entry : entries) {
			AttributeInstance ins = player.getAttribute(entry.attr());
			if (ins == null) continue;
			ResourceLocation id = entry.getId(this);
			if (ins.getModifier(id) != null) continue;
			double val = entry.getValue(rank);
			ins.addTransientModifier(new AttributeModifier(id, val, entry.op()));
		}
	}

	protected abstract T getData();

	@Override
	public T getData(ArtifactSetConfig.Entry ent) {
		T ans = getData();
		for (AttrSetEntry entry : entries) {
			var id = ent.effect().getRegistryName();
			ans.list.add(new AttributeSetData.AttributePair(entry.attr(), id));
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
			ans.add(ent.toComponent(rank));
		}
		return ans;
	}
}
