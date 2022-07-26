package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class PerfectionProtection extends SetEffect {

	private final LinearFuncEntry reduce;

	public PerfectionProtection(LinearFuncEntry reduce) {
		super(0);
		this.reduce = reduce;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int reduce = (int) Math.round(this.reduce.getFromRank(rank) * 100);
		return List.of(Component.translatable(getDescriptionId() + ".desc", reduce));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent hurt) {
		if (player.getHealth() < player.getMaxHealth()) return;
		if (!hurt.getSource().isBypassMagic()) {
			hurt.setAmount((float) (hurt.getAmount() * (1 - reduce.getFromRank(rank))));
		}
	}

}
