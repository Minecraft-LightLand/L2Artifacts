package dev.xkmc.l2artifacts.content.enchantments.sword;

import dev.xkmc.l2artifacts.events.AttackEventHandler;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TracingDamage extends SwordEnchant {

	public static final Supplier<Double> PERCENT_PER_LEVEL = ModConfig.COMMON.tracingRatio::get;

	private static final String KEY_ID = "TrackedEntity";
	private static final String KEY_DAMAGE = "StackedDamage";

	public TracingDamage() {
		super(TRACK_ENT);
	}

	@Override
	public int getAdditionalDamage(int lv, LivingHurtEvent event, AttackEventHandler.AttackCache attackCache) {
		UUID old = attackCache.weapon.getOrCreateTag().getUUID(KEY_ID);
		UUID current = attackCache.target.getUUID();
		if (current.equals(old)) {
			int count = attackCache.weapon.getOrCreateTag().getInt(KEY_DAMAGE) + 1;
			attackCache.weapon.getOrCreateTag().putInt(KEY_DAMAGE, count);
			return (int) Math.floor(event.getAmount() * ((float) Math.pow(1 + PERCENT_PER_LEVEL.get(), count) - 1));
		} else {
			attackCache.weapon.getOrCreateTag().putUUID(KEY_ID, current);
			attackCache.weapon.getOrCreateTag().putInt(KEY_DAMAGE, 0);
			return 0;
		}
	}


}
