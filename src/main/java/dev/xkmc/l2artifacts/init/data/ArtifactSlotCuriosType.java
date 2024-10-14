package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.Locale;

public enum ArtifactSlotCuriosType {
	HEAD("artifact_head", -1400),
	NECKLACE("artifact_necklace", -1390),
	BRACELET("artifact_bracelet", -1380),
	BODY("artifact_body", -1370),
	BELT("artifact_belt", -1360);

	final String id;
	public final int priority;

	public ArtifactSlot slot;

	ArtifactSlotCuriosType(String id, int priority) {
		this.id = id;
		this.priority = priority;
	}

	public String getIdentifier() {
		return this.id;
	}

	public String getDefTranslation() {
		return "Artifact - " + RegistrateLangProvider.toEnglishName(name().toLowerCase(Locale.ROOT));
	}

	public String getDesc() {
		return "curios.identifier." + id;
	}

	public String getModifier() {
		return "curios.modifiers." + id;
	}

	public void buildConfig(CuriosDataProvider cons) {
	}
}