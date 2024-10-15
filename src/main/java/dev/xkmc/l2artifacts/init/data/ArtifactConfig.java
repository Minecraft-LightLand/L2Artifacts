package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ArtifactConfig {


	public static class Common extends ConfigInit {

		public final ModConfigSpec.BooleanValue enableArtifactRankUpRecipe;


		Common(Builder builder) {
			markL2();
			enableArtifactRankUpRecipe = builder.text("Enable Artifact Rank up recipe")
					.define("enableArtifactRankUpRecipe", true);
		}

	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue maxRank;
		public final ModConfigSpec.IntValue maxLevelPerRank;
		public final ModConfigSpec.IntValue levelPerSubStat;

		public final ModConfigSpec.IntValue storageSmall;
		public final ModConfigSpec.IntValue storageLarge;

		public final ModConfigSpec.DoubleValue expConsumptionRankFactor;
		public final ModConfigSpec.DoubleValue expLevelFactor;
		public final ModConfigSpec.IntValue baseExpConsumption;
		public final ModConfigSpec.DoubleValue expRetention;
		public final ModConfigSpec.IntValue baseExpConversion;
		public final ModConfigSpec.DoubleValue expConversionRankFactor;

		public final ModConfigSpec.BooleanValue useLevelDropForHostility;
		public final ModConfigSpec.DoubleValue globalDropChanceMultiplier;

		Server(Builder builder) {
			markL2();
			maxRank = builder.text("maximum available rank (Not implemented. Don't change.)")
					.defineInRange("maxRank", 5, 5, 5);
			maxLevelPerRank = builder.text("maximum level per rank")
					.defineInRange("maxLevelPerRank", 4, 1, 100);
			levelPerSubStat = builder.text("level per sub stats granted (Not Tested. Don't change)")
					.defineInRange("levelPerSubStat", 4, 1, 100);
			storageSmall = builder.text("maximum available slots for artifact pocket")
					.defineInRange("storageSmall", 256, 64, 1024);
			storageLarge = builder.text("maximum available slots for upgraded artifact pocket")
					.defineInRange("storageLarge", 512, 64, 1024);

			expConsumptionRankFactor = builder.text("exponential experience requirement per rank")
					.defineInRange("expConsumptionRankFactor", 2d, 1, 10);
			expLevelFactor = builder.text("exponential experience requirement per level")
					.defineInRange("expLevelFactor", 1.05d, 1, 10);
			baseExpConsumption = builder.text("experience requirement for level 0 rank 1 artifact")
					.defineInRange("baseExpConsumption", 100, 1, 10000);
			expRetention = builder.text("experience retained for using upgraded artifact to upgrade")
					.defineInRange("expRetention", 0.9, 0, 1);
			baseExpConversion = builder.text("experience available for level 0 rank 1 artifact")
					.defineInRange("baseExpConversion", 100, 1, 1000000);
			expConversionRankFactor = builder.text("exponential experience available per rank")
					.defineInRange("expConversionRankFactor", 2d, 1, 10);

			useLevelDropForHostility = builder.text("When L2Hostility is installed, use level instead of health for drops")
					.comment("Min health requirement would still be effective")
					.define("useLevelDropForHostility", true);
			globalDropChanceMultiplier = builder.text("Reduce artifact drop chance by a factor")
					.comment("Stack multiplicatively with drop chance specified in datapack")
					.defineInRange("globalDropChanceMultiplier", 1, 0, 10d);
		}

	}

	public static final Common COMMON = L2Artifacts.REGISTRATE.registerUnsynced(Common::new);
	public static final Server SERVER = L2Artifacts.REGISTRATE.registerSynced(Server::new);

	public static void init() {
	}

}
