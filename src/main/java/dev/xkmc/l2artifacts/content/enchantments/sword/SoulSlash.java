package dev.xkmc.l2artifacts.content.enchantments.sword;

import dev.xkmc.l2artifacts.events.AttackEventHandler;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.function.Supplier;

public class SoulSlash extends SwordEnchant {

	public static final Supplier<Double> CHANCE_PER_LEVEL = ModConfig.COMMON.soulSlashChance::get;

	public SoulSlash() {
		super(SOUL_SLASH);
	}

	@Override
	public void onTargetAttacked(int lv, LivingAttackEvent event, AttackEventHandler.AttackCache attackCache) {
		if (RANDOM.nextFloat() < lv * CHANCE_PER_LEVEL.get()) event.getSource().bypassArmor();
	}
}
