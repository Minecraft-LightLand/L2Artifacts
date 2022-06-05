package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.enchantments.sword.SwordEnchant;
import dev.xkmc.l2artifacts.init.ModEntryPoint;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;

public class AttackEventHandler {

	public static class AttackCache {

		private AttackEntityEvent player;
		private LivingAttackEvent attack;
		private LivingHurtEvent hurt;
		private LivingDamageEvent damage;

		public LivingEntity target;
		public LivingEntity attacker;
		public ItemStack weapon;

		public float damage_1, damage_2;

		private void clear() {
			player = null;
			target = null;
			attack = null;
			hurt = null;
			damage = null;
			attacker = null;
			weapon = null;
			damage_1 = damage_2 = 0;
		}

		private void pushPlayer(AttackEntityEvent event) {
			player = event;
		}

		private void pushAttack(LivingAttackEvent event) {
			attack = event;
			target = attack.getEntityLiving();
			if (weapon != null) {
				for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(weapon).entrySet()) {
					if (ent.getKey() instanceof SwordEnchant sword) {
						sword.onTargetAttacked(ent.getValue(), event, this);
					}
				}
			}
		}

		private void pushHurt(LivingHurtEvent event) {
			if (event.getEntityLiving() != target) {
				ModEntryPoint.LOGGER.error("incorrect sequence: previous cache is for " + target + ", but this is for " + event.getEntityLiving());
				clear();
				return;
			}
			hurt = event;
			damage_1 = event.getAmount();
			if (weapon != null) {
				for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(weapon).entrySet()) {
					if (ent.getKey() instanceof SwordEnchant sword) {
						damage_1 += sword.getAdditionalDamage(ent.getValue(), event, this);
					}
				}
				for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(weapon).entrySet()) {
					if (ent.getKey() instanceof SwordEnchant sword) {
						sword.onTargetHurt(ent.getValue(), event, this);
					}
				}
			}
		}

		private void pushDamage(LivingDamageEvent event) {
			if (event.getEntityLiving() != target) {
				ModEntryPoint.LOGGER.error("incorrect sequence: previous cache is for " + target + ", but this is for " + event.getEntityLiving());
				clear();
				return;
			}
			damage = event;
			damage_2 = event.getAmount();
			if (weapon != null) {
				for (Map.Entry<Enchantment, Integer> ent : EnchantmentHelper.getEnchantments(weapon).entrySet()) {
					if (ent.getKey() instanceof SwordEnchant sword) {
						sword.onTargetDamage(ent.getValue(), event, this);
					}
				}
			}
		}

		private void setupAttackerProfile(LivingEntity entity, ItemStack stack) {
			attacker = entity;
			weapon = stack;
		}

	}

	private static final AttackCache cache = new AttackCache();

	@SubscribeEvent
	public static void onPlayerAttack(AttackEntityEvent event) {

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onAttackPre(LivingAttackEvent event) {
		cache.pushAttack(event);
		DamageSource source = event.getSource();
		if (source.getEntity() instanceof LivingEntity entity) { // direct damage only
			ItemStack stack = entity.getMainHandItem();
			cache.setupAttackerProfile(entity, stack);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onActuallyHurtPre(LivingHurtEvent event) {
		cache.pushHurt(event);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onDamagePre(LivingDamageEvent event) {
		cache.pushDamage(event);
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {

	}

}
