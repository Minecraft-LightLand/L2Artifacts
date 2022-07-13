package dev.xkmc.l2artifacts.content.effects.general;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
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
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		int reduce = (int) Math.round(this.reduce.getFromRank(item.rank) * 100);
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", reduce)));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent hurt) {
		if (player.getHealth() < player.getMaxHealth()) return;
		if (!hurt.getSource().isBypassMagic()) {
			hurt.setAmount((float) (hurt.getAmount() * (1 - reduce.getFromRank(rank))));
		}
	}

}
