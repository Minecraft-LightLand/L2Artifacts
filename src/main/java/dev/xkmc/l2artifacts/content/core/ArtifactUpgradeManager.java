package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.init.data.ModConfig;

public class ArtifactUpgradeManager {

	public static int getExpForLevel(int rank, int level) {
		double rank_factor = ModConfig.COMMON.expConsumptionRankFactor.get();
		double level_factor = ModConfig.COMMON.expLevelFactor.get();
		double base = ModConfig.COMMON.baseExpConsumption.get();
		return (int) Math.round(base * Math.pow(level_factor, level) * Math.pow(rank_factor, rank - 1));
	}

	public static int getExpForConversion(ArtifactStats stat) {
		int base = ModConfig.COMMON.baseExpConversion.get();
		double base_factor = ModConfig.COMMON.expConversionRankFactor.get();
		double retention = ModConfig.COMMON.expRetention.get();
		double base_exp = base * Math.pow(base_factor, stat.rank - 1);
		double used_exp = stat.exp;
		for (int i = 0; i < stat.level; i++) {
			used_exp += getExpForLevel(stat.rank, stat.level);
		}
		return (int) Math.round(base_exp + used_exp * retention);
	}

	public static int getMaxLevel(int rank) {
		return rank * ModConfig.COMMON.maxLevelPerRank.get();
	}

	public static void onUpgrade(ArtifactStats stats) {
		int gate = ModConfig.COMMON.levelPerSubStat.get();
		//TODO upgrade main stats
		if (stats.level % gate == 0) {
			//TODO upgrade sub stats
		}
	}
}
