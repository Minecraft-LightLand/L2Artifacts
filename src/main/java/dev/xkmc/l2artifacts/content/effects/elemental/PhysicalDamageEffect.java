package dev.xkmc.l2artifacts.content.effects.elemental;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.AttributeSetEffect;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class PhysicalDamageEffect extends AttributeSetEffect {

	public static final double FACTOR = 0.5;//TODO config

	public PhysicalDamageEffect() {
		super(new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_TOTAL, 0.2, 0.1, true));
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		var ans = super.getDetailedDescription(item);
		ans.add(Component.translatable(getDescriptionId() + ".desc", FACTOR));
		return ans;
	}

	@Override
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		LivingHurtEvent hurt = event.getLivingHurtEvent();
		assert hurt != null;
		if (hurt.getSource().isMagic()) {
			event.setDamageModified((float) (event.getDamageModified() * FACTOR));
		}
	}
}
