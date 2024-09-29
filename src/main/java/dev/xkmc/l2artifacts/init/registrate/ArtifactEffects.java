package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.mobeffects.FleshOvergrowth;
import dev.xkmc.l2artifacts.content.mobeffects.FrostShield;
import dev.xkmc.l2artifacts.content.mobeffects.FungusInfection;
import dev.xkmc.l2artifacts.content.mobeffects.ThermalMotive;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ArtifactEffects {

	public static final SimpleEntry<MobEffect> FLESH_OVERGROWTH = genEffect("flesh_overgrowth",
			() -> new FleshOvergrowth(MobEffectCategory.NEUTRAL, 0xffffffff),
			"Increase max health and damage taken");

	public static final SimpleEntry<MobEffect> FUNGUS = genEffect("fungus_infection",
			() -> new FungusInfection(MobEffectCategory.HARMFUL, 0xffffffff),
			"Reduce max health, drop fungus on death");

	public static final SimpleEntry<MobEffect> THERMAL_MOTIVE = genEffect("thermal_motive",
			() -> new ThermalMotive(MobEffectCategory.BENEFICIAL, 0xffffffff),
			"increase attack damage");

	public static final SimpleEntry<MobEffect> FROST_SHIELD = genEffect("frost_shield",
			() -> new FrostShield(MobEffectCategory.BENEFICIAL, 0xffffffff),
			"reduce damage taken");

	private static <T extends MobEffect> SimpleEntry<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(L2Artifacts.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {
	}

}
