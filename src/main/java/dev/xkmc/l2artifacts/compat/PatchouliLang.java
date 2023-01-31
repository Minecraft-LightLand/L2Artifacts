package dev.xkmc.l2artifacts.compat;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.init.L2Artifacts;

public enum PatchouliLang {
	TITLE("title", "L2Artifacts Guide"),
	LANDING("landing", "Welcome to L2Artifact");

	private final String key, def;

	PatchouliLang(String key, String def) {
		this.key = "patchouli." + L2Artifacts.MODID + "." + key;
		this.def = def;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (PatchouliLang lang : PatchouliLang.values()) {
			pvd.add(lang.key, lang.def);
		}
	}

}
