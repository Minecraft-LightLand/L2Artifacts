package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2library.base.NamedEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

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

	/**
	 * 获取词条描述
	 */
	public List<MutableComponent> getDetailedDescription(int rank) {
		return List.of(Component.translatable(getDescriptionId() + ".desc"));
	}

	/**
	 * 当玩家试图发动近战攻击时触发，可以修改伤害数值。此时目标已确定，但是伤害来源还未创建。
	 */
	public boolean playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
		return false;
	}

	/**
	 * 当玩家被攻击时触发
	 */
	public void playerAttackedEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
		var event = cache.getLivingAttackEvent();
		assert event != null;
		var source = event.getSource();
		if (!event.isCanceled() && !source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			if (playerAttackedCancel(player, ent, rank, source, cache)) {
				event.setCanceled(true);
			}
		}
	}

	public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		return false;
	}

	/**
	 * 当玩家收到伤害时触发，可以修改伤害数值。
	 */
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache cache) {
		var event = cache.getLivingDamageEvent();
		assert event != null;
		var source = event.getSource();
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
			playerReduceDamage(player, ent, rank, source, cache);
		}
	}

	public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {

	}

	/**
	 * 当玩家试图对怪物造成伤害时触发，可以修改伤害数值。此时已通过怪物的免疫判定，但是还未处理怪物减伤判定。
	 */
	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {

	}

	/**
	 * 当玩家对怪物造成确实伤害时触发。此时已处理过怪物减伤判定。
	 */
	public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
	}

	/**
	 * 当玩家击杀怪物时触发。
	 */
	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {

	}


}
