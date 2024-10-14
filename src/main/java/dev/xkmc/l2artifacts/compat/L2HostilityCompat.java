package dev.xkmc.l2artifacts.compat;

import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.L2Hostility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.fml.ModList;

public class L2HostilityCompat {

	public static boolean validForDrop(LivingEntity e, int min, int max) {
		if (e instanceof Mob mob && ModList.get().isLoaded(L2Hostility.MODID) &&
				ArtifactConfig.COMMON.useLevelDropForHostility.get()) {
			return validForDropForHostility(mob, min, max);
		}
		if (e instanceof Enemy) {
			float health = e.getMaxHealth();
			return health + 1e-3 > min && (max <= 0 || health + 1e-3 < max);
		}
		return false;
	}

	private static boolean validForDropForHostility(Mob e, int min, int max) {
		var opt = e.getCapability(MobTraitCap.CAPABILITY).resolve();
		if (opt.isEmpty()) {
			return false;
		}
		int lv = opt.get().getLevel();
		float health = e.getMaxHealth();
		boolean overHealth = health + 1e-3 > min;
		boolean underHealth = health + 1e-3 < max;
		boolean overLevel = lv >= min;
		boolean underLevel = lv < max;
		if (!overHealth || !overLevel) return false;
		return max <= 0 || underHealth || underLevel;
	}


}
