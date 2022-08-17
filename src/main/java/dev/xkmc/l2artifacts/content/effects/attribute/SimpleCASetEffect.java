package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class SimpleCASetEffect extends AbstractCASetEffect<AttributeSetData> {

	private final Predicate<Player> pred;

	public SimpleCASetEffect(Predicate<Player> pred, AttrSetEntry... entries) {
		super(entries);
		this.pred = pred;
	}

	@Override
	protected void tickData(Player player, ArtifactSetConfig.Entry ent, int rank, AttributeSetData data) {
		if (!pred.test(player)) return;
		data.update(2, rank);
		addAttributes(player, ent, rank, data);
	}

	@Override
	public AttributeSetData getData() {
		return new AttributeSetData();
	}

}
