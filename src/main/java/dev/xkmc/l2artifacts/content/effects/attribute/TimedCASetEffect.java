package dev.xkmc.l2artifacts.content.effects.attribute;


import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class TimedCASetEffect extends AbstractCASetEffect<TimedCAData> {

	private final Predicate<Player> pred;

	private final LinearFuncEntry period;

	public TimedCASetEffect(Predicate<Player> pred, LinearFuncEntry period, AttrSetEntry... entries) {
		super(entries);
		this.pred = pred;
		this.period = period;
	}


	protected MutableComponent getConditionText(int rank) {
		double time = Math.round(period.getFromRank(rank) * 5) / 20d;
		return Component.translatable(getDescriptionId() + ".desc", time);
	}

	@Override
	protected TimedCAData getData() {
		return new TimedCAData();
	}
}
