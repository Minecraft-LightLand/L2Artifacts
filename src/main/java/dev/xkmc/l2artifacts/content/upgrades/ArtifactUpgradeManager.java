package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;

import javax.annotation.Nullable;

public class ArtifactUpgradeManager {

	public static int getExpForLevel(int rank, int level) {
		double rank_factor = ArtifactConfig.COMMON.expConsumptionRankFactor.get();
		double level_factor = ArtifactConfig.COMMON.expLevelFactor.get();
		double base = ArtifactConfig.COMMON.baseExpConsumption.get();
		return (int) Math.round(base * Math.pow(level_factor, level) * Math.pow(rank_factor, rank - 1));
	}

	public static int getExpForConversion(int rank, @Nullable ArtifactStats stat) {
		int base = ArtifactConfig.COMMON.baseExpConversion.get();
		double base_factor = ArtifactConfig.COMMON.expConversionRankFactor.get();
		double retention = ArtifactConfig.COMMON.expRetention.get();
		double base_exp = base * Math.pow(base_factor, rank - 1);
		if (stat == null) {
			return (int) Math.round(base_exp);
		}
		double used_exp = stat.exp();
		for (int i = 0; i < stat.level(); i++) {
			used_exp += getExpForLevel(stat.rank(), i);
		}
		return (int) Math.round(base_exp + used_exp * retention);
	}

	public static int getMaxLevel(int rank) {
		return rank * ArtifactConfig.COMMON.maxLevelPerRank.get();
	}

}
