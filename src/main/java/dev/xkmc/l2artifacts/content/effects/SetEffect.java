package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
	 * 古遗物等级或者数量改变时触发，包括移除
	 */
	public void update(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	/**
	 * always ticks regardless if it's enabled or not
	 * 每帧触发，即使没启动
	 */
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
	}

	/**
	 * 获取词条描述
	 */
	public List<MutableComponent> getDetailedDescription(int rank) {
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc")));
	}

	/**
	 * 当玩家试图发动近战攻击时触发，可以修改伤害数值。此时目标已确定，但是伤害来源还未创建。
	 */
	public void playerAttackModifyEvent(Player player, ArtifactSetConfig.Entry ent, int rank, CriticalHitEvent event) {

	}

	/**
	 * 当玩家收到伤害时触发，可以修改伤害数值。
	 */
	public void playerHurtEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingHurtEvent event) {

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
