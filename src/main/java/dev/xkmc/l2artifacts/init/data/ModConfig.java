package dev.xkmc.l2artifacts.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {

	public static class Common {

		public final ForgeConfigSpec.IntValue maxRank;
		public final ForgeConfigSpec.IntValue maxLevelPerRank;
		public final ForgeConfigSpec.IntValue levelPerSubStat;

		public final ForgeConfigSpec.DoubleValue expConsumptionRankFactor;
		public final ForgeConfigSpec.DoubleValue expLevelFactor;
		public final ForgeConfigSpec.IntValue baseExpConsumption;
		public final ForgeConfigSpec.DoubleValue expRetention;
		public final ForgeConfigSpec.IntValue baseExpConversion;
		public final ForgeConfigSpec.DoubleValue expConversionRankFactor;

		Common(ForgeConfigSpec.Builder builder) {
			maxRank = builder.comment("maximum available rank")
					.defineInRange("maxRank", 5, 1, 10);
			maxLevelPerRank = builder.comment("maximum level per rank")
					.defineInRange("maxLevelPerRank", 4, 1, 100);
			levelPerSubStat = builder.comment("level per sub stats granted")
					.defineInRange("levelPerSubStat", 4, 1, 100);

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
