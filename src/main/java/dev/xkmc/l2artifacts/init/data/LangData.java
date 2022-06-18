package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum LangData {
	RAW_ARTIFACT("tooltip.raw_artifact", "Right Click to Reveal Stats", 0, null),
	ARTIFACT_LEVEL("tooltip.artifact_level", "Level: %s", 1, null),
	ARTIFACT_EXP("tooltip.artifact_exp", "Exp: %s/%s", 2, ChatFormatting.DARK_GRAY),
	UPGRADE("tooltip.upgrade", "Right Click to Reveal Upgrade Result", 0, ChatFormatting.GOLD),
	MAIN_STAT("tooltip.main_stat", "Main Stats", 0, ChatFormatting.GRAY),
	SUB_STAT("tooltip.sub_stat", "Sub Stats", 0, ChatFormatting.GRAY),
	EXP_CONVERSION("tooltip.exp_conversion", "Exp as fodder: %s", 1, ChatFormatting.DARK_GRAY);

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	LangData(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = L2Artifacts.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public static TranslatableComponent getTranslate(String s) {
		return new TranslatableComponent(L2Artifacts.MODID + "." + s);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		TranslatableComponent ans = new TranslatableComponent(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (LangData lang : LangData.values()) {
			pvd.add(lang.key, lang.def);
		}
		pvd.add("itemGroup." + L2Artifacts.MODID + ".artifacts", "Artifacts");
		pvd.add("attribute.name.crit_rate", "Crit Rate");
		pvd.add("attribute.name.crit_damage", "Crit Damage");
		pvd.add("l2artifacts.set.1", "When Equip: ");
		pvd.add("l2artifacts.set.2", "When Equip 2 of this Set: ");
		pvd.add("l2artifacts.set.3", "When Equip 3 of this Set: ");
		pvd.add("l2artifacts.set.4", "When Equip 4 of this Set: ");
		pvd.add("l2artifacts.set.5", "When Equip 5 of this Set: ");

		pvd.add("set_effect.l2artifacts.perfection_absorption.desc", "When at full health, gain %s points of absorption");
		pvd.add("set_effect.l2artifacts.perfection_protection.desc", "When at full health, reduce damage by %s%%");
		pvd.add("set_effect.l2artifacts.damocles.desc", "When at full health, direct attack damage increase by %s%%. When below half health, die immediately.");
		pvd.add("set_effect.l2artifacts.saint_reduction.desc", "Direct damage dealt reduce by %s%%, damage taken reduce b %s%%");
		pvd.add("set_effect.l2artifacts.saint_restoration.desc", "When have empty main hand, restore health to oneself or allies every %s seconds.");
		pvd.add("set_effect.l2artifacts.protection_resistance.desc", "Damage taken reduced when health is low.");
	}

}
