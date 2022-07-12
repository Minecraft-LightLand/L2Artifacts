package dev.xkmc.l2artifacts.content.effects.general;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import java.util.List;

public class SaintReduction extends SetEffect {

	private final double base, slope;

	public SaintReduction(double base, double slope) {
		super(0);
		this.base = base;
		this.slope = slope;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		int damage = (int) Math.round(100 * (1 - base));
		int reduction = (int) Math.round(100 * (1 - base - slope * item.rank));
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", damage, reduction)));
	}

	@Override
	public void playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent crit) {
		crit.setDamageModifier((float) (crit.getDamageModifier() * (1 - base)));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent hurt) {
		if (!hurt.getSource().isBypassMagic()) {
			float amp = (float) (1 - base - slope * rank);
			hurt.setAmount(hurt.getAmount() * amp);
		}
	}
}
