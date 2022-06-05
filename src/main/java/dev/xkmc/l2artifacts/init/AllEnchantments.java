package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.content.enchantments.armor.Digest;
import dev.xkmc.l2artifacts.content.enchantments.armor.FastLeg;
import dev.xkmc.l2artifacts.content.enchantments.armor.InvisibleArmor;
import dev.xkmc.l2artifacts.content.enchantments.armor.StableBody;
import dev.xkmc.l2artifacts.content.enchantments.core.BaseEnchantment;
import dev.xkmc.l2artifacts.content.enchantments.sword.*;
import dev.xkmc.l2artifacts.content.enchantments.tool.LifeSync;
import dev.xkmc.l2artifacts.content.enchantments.tool.Reach;
import dev.xkmc.l2artifacts.content.enchantments.tool.Remnant;
import dev.xkmc.l2artifacts.content.enchantments.tool.Robust;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.Supplier;

import static dev.xkmc.l2artifacts.init.ModEntryPoint.REGISTRATE;

public class AllEnchantments {

	public static final RegistryEntry<AntiMagic> ANTI_MAGIC = reg("anti_magic", AntiMagic::new);
	public static final RegistryEntry<SoulSlash> SOUL_SLASH = reg("soul_slash", SoulSlash::new);
	public static final RegistryEntry<StackingDamage> STACK_DMG = reg("stacking_damage", StackingDamage::new);
	public static final RegistryEntry<TracingDamage> TRACK_ENT = reg("tracing_damage", TracingDamage::new);
	public static final RegistryEntry<Fragile> FRAGILE = reg("fragile", Fragile::new);
	public static final RegistryEntry<LightSwing> LIGHT_SWING = reg("light_swing", LightSwing::new);
	public static final RegistryEntry<HeavySwing> HEAVY_SWING = reg("heavy_swing", HeavySwing::new);
	public static final RegistryEntry<WindSweep> WIND_SWEEP = reg("wind_sweep", WindSweep::new);

	public static final RegistryEntry<Remnant> REMNANT = reg("remnant", Remnant::new);
	public static final RegistryEntry<Robust> ROBUST = reg("robust", Robust::new);
	public static final RegistryEntry<Reach> REACH = reg("reach", Reach::new);
	public static final RegistryEntry<LifeSync> LIFE_SYNC = reg("life_sync", LifeSync::new);

	public static final RegistryEntry<StableBody> STABLE_BODY = reg("stable_body", StableBody::new);
	public static final RegistryEntry<FastLeg> FAST_LEG = reg("fast_leg", FastLeg::new);
	public static final RegistryEntry<InvisibleArmor> INVISIBLE_ARMOR = reg("invisible_armor", InvisibleArmor::new);
	public static final RegistryEntry<Digest> DIGEST = reg("digest", Digest::new);

	public static void register() {

	}

	private static <T extends BaseEnchantment> RegistryEntry<T> reg(String id, Supplier<T> sup) {
		return REGISTRATE.enchantment(id, EnchantmentCategory.BREAKABLE, (a, b, c) -> sup.get()).defaultLang().register();

	}

}
