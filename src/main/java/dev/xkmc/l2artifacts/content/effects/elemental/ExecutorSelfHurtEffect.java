package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;

public class ExecutorSelfHurtEffect extends AttributeSetEffect {

	public static final double FACTOR = 0.2;//TODO config

	public ExecutorSelfHurtEffect() {
		super(new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_TOTAL, 0.3, 0.15, true));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		var ans = super.getDetailedDescription(item);
		ans.add(Component.translatable(getDescriptionId() + ".desc"));
		return ans;
	}

	@Override
	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		double damage = event.getEntityLiving().getMaxHealth() * FACTOR;
		player.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic(), (float) damage);
	}
}
