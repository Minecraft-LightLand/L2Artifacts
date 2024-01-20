package dev.xkmc.l2artifacts.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

	public static class Common {

		public final ForgeConfigSpec.IntValue maxRank;
		public final ForgeConfigSpec.IntValue maxLevelPerRank;
		public final ForgeConfigSpec.IntValue levelPerSubStat;

		public final ForgeConfigSpec.IntValue storageSmall;
		public final ForgeConfigSpec.IntValue storageLarge;

		public final ForgeConfigSpec.DoubleValue expConsumptionRankFactor;
		public final ForgeConfigSpec.DoubleValue expLevelFactor;
		public final ForgeConfigSpec.IntValue baseExpConsumption;
		public final ForgeConfigSpec.DoubleValue expRetention;
		public final ForgeConfigSpec.IntValue baseExpConversion;
		public final ForgeConfigSpec.DoubleValue expConversionRankFactor;

		public final ForgeConfigSpec.BooleanValue showArtifactAttributeTooltip;

		Common(ForgeConfigSpec.Builder builder) {
			maxRank = builder.comment("maximum available rank (Not implemented. Don't change.)")
					.defineInRange("maxRank", 5, 1, 10);
			maxLevelPerRank = builder.comment("maximum level per rank (Not tested. Don't change)")
					.defineInRange("maxLevelPerRank", 4, 1, 100);
			levelPerSubStat = builder.comment("level per sub stats granted (Not Tested. Don't change)")
					.defineInRange("levelPerSubStat", 4, 1, 100);
			storageSmall = builder.comment("maximum available slots for artifact pocket")
					.defineInRange("storageSmall", 256, 64, 1024);
			storageLarge = builder.comment("maximum available slots for upgraded artifact pocket")
					.defineInRange("storageLarge", 512, 64, 1024);

			expConsumptionRankFactor = builder.comment("exponential experience requirement per rank")
					.defineInRange("expConsumptionRankFactor", 2d, 1, 10);
			expLevelFactor = builder.comment("exponential experience requirement per level")
					.defineInRange("expLevelFactor", 1.05d, 1, 10);
			baseExpConsumption = builder.comment("experience requirement for level 0 rank 1 artifact")
					.defineInRange("baseExpConsumption", 100, 1, 10000);
			expRetention = builder.comment("experience retained for using upgraded artifact to upgrade")
					.defineInRange("expRetention", 0.9, 0, 1);
			baseExpConversion = builder.comment("experience available for level 0 rank 1 artifact")
					.defineInRange("baseExpConversion", 100, 1, 1000000);
			expConversionRankFactor = builder.comment("exponential experience available per rank")
					.defineInRange("expConversionRankFactor", 2d, 1, 10);
			showArtifactAttributeTooltip = builder.comment("Show Artifact Attribute Tooltip again at the bottom of tooltip")
					.define("showArtifactAttributeTooltip", false);
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
