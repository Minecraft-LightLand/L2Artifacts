package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class SimpleCASetEffect extends AbstractConditionalAttributeSetEffect<AttributeSetData> {

	private final Predicate<Player> pred;

	public SimpleCASetEffect(Predicate<Player> pred, AttrSetEntry... entries) {
		super(entries);
		this.pred = pred;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled) return;
		if (!pred.test(player)) return;
		AttributeSetData data = fetch(player, ent);
		data.update(2, rank);
		addAttributes(player, ent, rank, data);
	}

	@Override
	public AttributeSetData getData() {
		return new AttributeSetData();
	}

}