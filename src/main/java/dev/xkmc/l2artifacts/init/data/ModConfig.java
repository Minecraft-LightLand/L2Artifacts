package dev.xkmc.l2artifacts.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

	public static class Common {

		public final ForgeConfigSpec.DoubleValue antiMagicChance;
		public final ForgeConfigSpec.DoubleValue soulSlashChance;
		public final ForgeConfigSpec.DoubleValue stackingRatio;
		public final ForgeConfigSpec.DoubleValue tracingRatio;
		public final ForgeConfigSpec.DoubleValue fragileDamageFactor;
		public final ForgeConfigSpec.DoubleValue fragileDurabilityFactor;
		public final ForgeConfigSpec.DoubleValue lightSwingDamage;
		public final ForgeConfigSpec.DoubleValue lightSwingSpeed;
		public final ForgeConfigSpec.DoubleValue lightSwingDurabilityFactor;
		public final ForgeConfigSpec.DoubleValue heavySwingDamage;
		public final ForgeConfigSpec.DoubleValue heavySwingSpeed;
		public final ForgeConfigSpec.DoubleValue heavySwingDurabilityFactor;
		public final ForgeConfigSpec.DoubleValue reachAddition;
		public final ForgeConfigSpec.DoubleValue lifeSync;
		public final ForgeConfigSpec.DoubleValue windSweepIncrement;
		public final ForgeConfigSpec.DoubleValue stableBodyThreshold;
		public final ForgeConfigSpec.DoubleValue stableBodyResistance;
		public final ForgeConfigSpec.IntValue fastLegFoodThreshold;

		Common(ForgeConfigSpec.Builder builder) {
			antiMagicChance = builder.comment("Anti Magic chance")
					.defineInRange("antiMagicChance", 0.1, 0, 1);

			soulSlashChance = builder.comment("Soul Slash chance")
					.defineInRange("soulSlashChance", 0.1, 0, 1);

			stackingRatio = builder.comment("Stacking damage enchantment damage increment multiplier")
					.defineInRange("stackingRatio", 0.1, 0, 1);

			tracingRatio = builder.comment("Tracing damage enchantment increment base multiplier")
					.defineInRange("tracingRatio", 0.02, 0, 1);

			fragileDamageFactor = builder.comment("Fragile enchantment damage multiplier")
					.defineInRange("fragileDamageFactor", 0.1, 0, 1);
			fragileDurabilityFactor = builder.comment("Fragile enchantment durability multiplier")
					.defineInRange("fragileDurabilityFactor", 0.5, 0, 100);

			lightSwingDamage = builder.comment("Light swing enchantment damage reduction")
					.defineInRange("lightSwingDamage", 1d, 0, 100);
			lightSwingSpeed = builder.comment("Light swing enchantment speed increment")
					.defineInRange("lightSwingSpeed", 0.2, 0, 100);
			lightSwingDurabilityFactor = builder.comment("Light swing enchantment durability cost multiplier")
					.defineInRange("lightSwingDurabilityFactor", 0.5, 0, 100);

			heavySwingDamage = builder.comment("Heavy swing enchantment damage increment")
					.defineInRange("heavySwingDamage", 1d, 0, 100);
			heavySwingSpeed = builder.comment("Heavy swing enchantment speed reduction")
					.defineInRange("heavySwingSpeed", 0.2, 0, 100);
			heavySwingDurabilityFactor = builder.comment("Heavy swing enchantment durability multiplier")
					.defineInRange("heavySwingDurabilityFactor", 0.5, 0, 100);

			reachAddition = builder.comment("Reach enchantment value increment")
					.defineInRange("reachAddition", 0.5d, 0, 4);

			lifeSync = builder.comment("Threshold reduction for lifeSync per level")
					.defineInRange("lifeSync", 0.2d, 0, 1);

			windSweepIncrement = builder.comment("Wind Sweep hit box increment")
					.defineInRange("windSweep", 0.5, 0, 100);

			stableBodyThreshold = builder.comment("Threshold reduction for stableBody per level")
					.defineInRange("stableBodyThreshold", 0.2d, 0, 1);
			stableBodyResistance = builder.comment("Knockback resistance for stableBody per level")
					.defineInRange("stableBodyResistance", 0.2d, 0, 1);

			fastLegFoodThreshold = builder.comment("Threshold for fast leg enchantment")
					.defineInRange("fastLegFoodThreshold", 16, 0, 20);

		}

	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
	}


}
