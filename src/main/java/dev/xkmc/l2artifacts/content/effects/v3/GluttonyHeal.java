package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public class GluttonyHeal extends PlayerOnlySetEffect {

	private final LinearFuncEntry value;

	public GluttonyHeal(LinearFuncEntry value) {
		super();
		this.value = value;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int l = (int) Math.round(value.getFromRank(rank));
		return List.of(Component.translatable(getDescriptionId() + ".desc", l, l * 2));
	}

	@Override
	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		if (player.level().isClientSide()) return;
		int val = (int) Math.round(value.getFromRank(rank));
		player.getFoodData().eat(val, 1);
	}
}
