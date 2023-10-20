package dev.xkmc.l2artifacts.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ArtifactConfig {

	public static class Client {

		Client(ForgeConfigSpec.Builder builder) {
		}

	}

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

		Common(ForgeConfigSpec.Builder builder) {
			maxRank = builder.comment("maximum available rank (Not implemented. Don't change.)")
					.defineInRange("maxRank", 5, 5, 5);
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
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {

		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
		register(ModConfig.Type.COMMON, COMMON_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec<?> spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2_configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		ModLoadingContext.get().registerConfig(type, spec, path);
	}


}
