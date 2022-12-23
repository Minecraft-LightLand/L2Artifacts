package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import javax.annotation.Nullable;
import java.util.Locale;

public enum LangData {
	RAW_ARTIFACT("tooltip.raw_artifact", "Right Click to Reveal Stats", 0, null),
	ARTIFACT_LEVEL("tooltip.artifact_level", "Level: %s", 1, null),
	ARTIFACT_EXP("tooltip.artifact_exp", "Exp: %s/%s", 2, ChatFormatting.DARK_GRAY),
	UPGRADE("tooltip.upgrade", "Right Click to Reveal Upgrade Result", 0, ChatFormatting.GOLD),
	MAIN_STAT("tooltip.main_stat", "Main Stats", 0, ChatFormatting.GRAY),
	SUB_STAT("tooltip.sub_stat", "Sub Stats", 0, ChatFormatting.GRAY),
	EXP_CONVERSION("tooltip.exp_conversion", "Exp as fodder: %s", 1, ChatFormatting.DARK_GRAY),
	SHIFT_TEXT("tooltip.shift", "Hold Shift for set effects", 0, null),
	CTRL_TEXT("tooltip.ctrl", "Hold Ctrl for upgrades", 0, null),
	SET("tooltip.set", "Set: %s", 1, null),
	STAT_CAPTURE_INFO("tooltip.stat_container.capture",
			"Put it in Anvil left slot, put artifact of same rank on right slot, " +
					"to capture the main stat of the artifact.", 0, null),
	STAT_USE_INFO("tooltip.stat_container.apply",
			"Put it in Anvil right slot, put artifact of the same rank that contains this stat as substat on the left slot, " +
					"to ensure the next upgrade of sub stat will be this stat", 0, null),
	STAT_INFO("tooltip.stat_container.info", "Stat: %s", 1, null),
	BOOST_MAIN("tooltip.boost.main",
			"Put it in Anvil right slot, put artifact of the same rank on the left slot, " +
					"to ensure the next upgrade of main stat will be maximized", 0, null),
	BOOST_SUB("tooltip.boost.sub",
			"Put it in Anvil right slot, put artifact of the same rank on the left slot, " +
					"to ensure the next upgrade of sub stat will be maximized", 0, null),
	UPGRADE_STAT("tooltip.enhance.stat", "Next upgraded sub stat will be %s.", 1, null),
	UPGRADE_MAIN("tooltip.enhance.main", "Next %s upgraded main stat will be maximized.", 1, null),
	UPGRADE_SUB("tooltip.enhance.sub", "Next %s upgraded sub stat will be maximized.", 1, null),
	ALL_SET_EFFECTS("set.all_set_effects", "Set %s: %s piece(s)", 2, null),
	TITLE_SELECT_SET("title.select_set", "Select Artifact Set", 0, null),
	TITLE_SELECT_SLOT("title.select_slot", "Select Artifact Slot", 0, null),
	TITLE_SELECT_RANK("title.select_rank", "Select Artifact Rank", 0, null),

	FILTER_RANK("title.filter.rank", "Rank Filter", 0, null),
	FILTER_SLOT("title.filter.slot", "Slot Filter", 0, null),
	FILTER_SET("title.filter.set", "Set Filter", 0, null),
	FILTER_STAT("title.filter.stat", "Stat Filter", 0, null),
	;

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

	public static MutableComponent getTranslate(String s) {
		return MutableComponent.create(new TranslatableContents(L2Artifacts.MODID + "." + s));
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = MutableComponent.create(new TranslatableContents(key, args));
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
		pvd.add("attribute.name.bow_strength", "Bow Strength");
		pvd.add("l2artifacts.set.1", "When Equip: ");
		pvd.add("l2artifacts.set.2", "When Equip 2 of this Set: ");
		pvd.add("l2artifacts.set.3", "When Equip 3 of this Set: ");
		pvd.add("l2artifacts.set.4", "When Equip 4 of this Set: ");
		pvd.add("l2artifacts.set.5", "When Equip 5 of this Set: ");
		pvd.add("menu.tabs.set_effects", "Activated Set Effects");

	}

}
