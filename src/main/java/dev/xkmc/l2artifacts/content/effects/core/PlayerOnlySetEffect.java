package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public class PlayerOnlySetEffect extends SetEffect {

	public PlayerOnlySetEffect(int ids) {
		super(ids);
	}

	/**
	 * when the set count changes. Entry contains an uuid if one needs to add it. for Attributes, it must be transient
	 */
	public final void update(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (le instanceof Player pl) update(pl, ent, rank, enabled);
	}

	/**
	 * always ticks regardless if it's enabled or not
	 */
	public final void tick(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (le instanceof Player pl) tick(pl, ent, rank, enabled);
	}

	/**
	 * 当玩家试图发动近战攻击时触发，可以修改伤害数值。此时目标已确定，但是伤害来源还未创建。
	 */
	public final boolean playerAttackModifyEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
		if (le instanceof Player pl) return playerAttackModifyEvent(pl, ent, rank, event);
		return false;
	}

	/**
	 * 当玩家试图对怪物造成伤害时触发，可以修改伤害数值。此时已通过怪物的免疫判定，但是还未处理怪物减伤判定。
	 */
	public final void playerHurtOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (le instanceof Player pl) playerHurtOpponentEvent(pl, ent, rank, event);
	}

	/**
	 * 当玩家对怪物造成确实伤害时触发。此时已处理过怪物减伤判定。
	 */
	public final void playerDamageOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
		if (le instanceof Player pl) playerDamageOpponentEvent(pl, ent, rank, event);
	}

	/**
	 * 当玩家击杀怪物时触发。
	 */
	public final void playerKillOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		if (le instanceof Player pl) playerKillOpponentEvent(pl, ent, rank, event);
	}

	public final void playerShieldBlock(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, ShieldBlockEvent event) {
		if (le instanceof Player pl) playerShieldBlock(pl, ent, rank, event);
	}

	public final boolean playerAttackedCancel(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		if (le instanceof Player pl) return playerAttackedCancel(pl, ent, rank, source, cache);
		return false;
	}

	public final void playerReduceDamage(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		if (le instanceof Player pl) playerReduceDamage(pl, ent, rank, source, cache);
	}


	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	public boolean playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
		return false;
	}

	public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
		return false;
	}

	public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
	}

	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
	}

	public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
	}

	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
	}

	public void playerShieldBlock(Player player, ArtifactSetConfig.Entry entry, int i, ShieldBlockEvent event) {
	}

}
