package dev.xkmc.l2artifacts.content.effects.core;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

public class PlayerOnlySetEffect extends SetEffect {

	/**
	 * when the set count changes. Entry contains an uuid if one needs to add it. for Attributes, it must be transient
	 */
	@Override
	public final void update(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (le instanceof Player pl) update(pl, ent, rank, enabled);
	}

	/**
	 * always ticks regardless if it's enabled or not
	 */
	@Override
	public final void tick(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (le instanceof Player pl) tick(pl, ent, rank, enabled);
	}

	/**
	 * 当玩家试图发动近战攻击时触发，可以修改伤害数值。此时目标已确定，但是伤害来源还未创建。
	 */
	@Override
	public final boolean playerAttackModifyEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
		if (le instanceof Player pl) return playerAttackModifyEvent(pl, ent, rank, event);
		return false;
	}

	/**
	 * 当玩家试图对怪物造成伤害时触发，可以修改伤害数值。此时已通过怪物的免疫判定，但是还未处理怪物减伤判定。
	 */
	@Override
	public final void playerHurtOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
		if (le instanceof Player pl) playerHurtOpponentEvent(pl, ent, rank, event);
	}

	/**
	 * 当玩家对怪物造成确实伤害时触发。此时已处理过怪物减伤判定。
	 */
	@Override
	public final void playerDamageOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageData.DefenceMax event) {
		if (le instanceof Player pl) playerDamageOpponentEvent(pl, ent, rank, event);
	}

	/**
	 * 当玩家击杀怪物时触发。
	 */

	@Override
	public final void playerKillOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
		if (le instanceof Player pl) playerKillOpponentEvent(pl, ent, rank, event);
	}

	@Override
	public final void playerShieldBlock(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingShieldBlockEvent event) {
		if (le instanceof Player pl) playerShieldBlock(pl, ent, rank, event);
	}

	@Override
	public final boolean playerAttackedCancel(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Attack cache) {
		if (le instanceof Player pl) return playerAttackedCancel(pl, ent, rank, source, cache);
		return false;
	}

	@Override
	public final void playerReduceDamage(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
		if (le instanceof Player pl) playerReduceDamage(pl, ent, rank, source, cache);
	}


	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	public boolean playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {
		return false;
	}

	public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Attack cache) {
		return false;
	}

	public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, DamageData.Defence cache) {
	}

	public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, DamageData.Offence event) {
	}

	public void playerDamageOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, DamageData.DefenceMax event) {
	}

	public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
	}

	public void playerShieldBlock(Player player, ArtifactSetConfig.Entry entry, int i, LivingShieldBlockEvent event) {
	}

}
