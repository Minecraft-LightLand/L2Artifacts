package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.init.AllEnchantments;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;

public class LangGen {

	public static void genLang(RegistrateLangProvider pvd) {
		pvd.add(AllEnchantments.ANTI_MAGIC.get().getDescriptionId() + ".desc", "Have a chance to bypass protection and resistance. Conflicts with Soul Slash.");
		pvd.add(AllEnchantments.FRAGILE.get().getDescriptionId() + ".desc", "Lower durability, more damage. Conflicts with Unbreaking and Robust.");
		pvd.add(AllEnchantments.REMNANT.get().getDescriptionId() + ".desc", "Preserve item at 0 durability, but item lost attributes until repaired.");
		pvd.add(AllEnchantments.ROBUST.get().getDescriptionId() + ".desc", "Reduce durability cost for high durability cost action.");
		pvd.add(AllEnchantments.SOUL_SLASH.get().getDescriptionId() + ".desc", "Has a chance to bypass armor. Conflicts with Anti Magic.");
		pvd.add(AllEnchantments.STACK_DMG.get().getDescriptionId() + ".desc", "When damage is reduced, next attack increase a portion of reduced damage. Conflicts with Tracing Damage.");
		pvd.add(AllEnchantments.TRACK_ENT.get().getDescriptionId() + ".desc", "When attacking the same target, increase damage exponentially. Conflicts with Stacking Damage.");
		pvd.add(AllEnchantments.LIGHT_SWING.get().getDescriptionId() + ".desc", "Increase attack speed at the cost of damage and durability. Conflicts with Heavy Swing.");
		pvd.add(AllEnchantments.HEAVY_SWING.get().getDescriptionId() + ".desc", "Increase damage and durability at the cost of attack speed. Conflicts with Light Swing.");
		pvd.add(AllEnchantments.REACH.get().getDescriptionId() + ".desc", "Increase reach distance, effective on main hand tools and armors.");
		pvd.add(AllEnchantments.LIFE_SYNC.get().getDescriptionId() + ".desc", "When health is sufficient, cost health to repair item. Level up to lower the health threshold.");
		pvd.add(AllEnchantments.WIND_SWEEP.get().getDescriptionId() + ".desc", "Increase Sweeping Edge radius.");
		pvd.add(AllEnchantments.STABLE_BODY.get().getDescriptionId() + ".desc", "Increase knockback resistance, cancel screen shaking when health is high.");
		pvd.add(AllEnchantments.FAST_LEG.get().getDescriptionId() + ".desc", "Give speed effect at high food level.");
		pvd.add(AllEnchantments.INVISIBLE_ARMOR.get().getDescriptionId() + ".desc", "Armor invisible to mobs and players.");
		pvd.add(AllEnchantments.DIGEST.get().getDescriptionId() + ".desc", "When saturation is low, cost 2 food level to restore 1 saturation level.");
	}

}
