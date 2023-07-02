package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class SaintReduction extends SetEffect {

	private final LinearFuncEntry atk, def;

	public SaintReduction(LinearFuncEntry atk, LinearFuncEntry def) {
		super(0);
		this.atk = atk;
		this.def = def;
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		int damage = (int) Math.round(100 * (1 - atk.getFromRank(rank)));
		int reduction = (int) Math.round(100 * (1 - def.getFromRank(rank)));
		return List.of(Component.translatable(getDescriptionId() + ".desc", damage, reduction));
	}

	@Override
	public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		event.addHurtModifier(DamageModifier.multBase((float) -atk.getFromRank(rank)));
	}

	@Override
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent hurt) {
		if (!hurt.getSource().is(DamageTypeTags.BYPASSES_EFFECTS)) {
			float amp = (float) (1 - def.getFromRank(rank));
			hurt.setAmount(hurt.getAmount() * amp);
		}
	}
}
