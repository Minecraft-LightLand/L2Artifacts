package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public abstract class SetEffect extends NamedEntry<SetEffect> {

	public final int ids;

	public SetEffect(int ids) {
		super(ArtifactTypeRegistry.SET_EFFECT);
		this.ids = ids;
	}

	/**
	 * when the set count changes. Entry contains an uuid if one needs to add it. for Attributes, it must be transient
	 */
	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	/**
	 * always ticks regardless if it's enabled or not
	 */
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc")));
	}

	public void playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {

	}

	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {

	}

	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {

	}

	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {

	}


}
