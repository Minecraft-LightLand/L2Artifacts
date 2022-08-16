package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.capability.SetEffectData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class ConditionalAttributeSetEffect extends PersistentDataSetEffect<SetEffectData> {

	private final Predicate<Player> pred;
	private final AttrSetEntry[] entries;

	public ConditionalAttributeSetEffect(Predicate<Player> pred, AttrSetEntry... entries) {
		super(entries.length);
		this.pred = pred;
		this.entries = entries;
	}

	@Override
	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		onRemove(player, ent); // refresh
	}

	@Override
	protected void tickData(Player player, ArtifactSetConfig.Entry ent, int rank, SetEffectData data) {
		if (!pred.test(player)) return;
		data.update(2, rank);
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			AttributeInstance ins = player.getAttribute(entry.attr().get());
			if (ins == null) continue;
			UUID id = ent.id[i];
			if (ins.getModifier(id) != null) continue;
			double val = entry.getValue(rank);
			ins.addTransientModifier(new AttributeModifier(id, ent.getName(), val, entry.op()));
		}
		data.onRemove = () -> onRemove(player, ent);
	}

	private void onRemove(Player player, ArtifactSetConfig.Entry ent) {
		for (int i = 0; i < entries.length; i++) {
			AttrSetEntry entry = entries[i];
			AttributeInstance ins = player.getAttribute(entry.attr().get());
			if (ins == null) continue;
			UUID id = ent.id[i];
			ins.removeModifier(id);
		}
	}

	@Override
	public SetEffectData getData() {
		return new SetEffectData();
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		ans.add(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc")));
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
