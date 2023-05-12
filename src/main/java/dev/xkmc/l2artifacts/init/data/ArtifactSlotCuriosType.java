package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.SlotTypeMessage;

import java.util.Locale;

public enum ArtifactSlotCuriosType {
	HEAD("artifact_head", -1400),
	NECKLACE("artifact_necklace", -1390),
	BRACELET("artifact_bracelet", -1380),
	BODY("artifact_body", -1370),
	BELT("artifact_belt", -1360);

	final String id;
	final int priority;

	ArtifactSlotCuriosType(String id, int priority) {
		this.id = id;
		this.priority = priority;
	}

	public String getIdentifier() {
		return this.id;
	}

	public String getDefTranslation() {
		return RegistrateLangProvider.toEnglishName(name().toLowerCase(Locale.ROOT));
	}

	public String getDesc() {
		return "curios.identifier." + id;
	}

	public SlotTypeMessage.Builder getMessageBuilder() {
		return new SlotTypeMessage.Builder(this.id).priority(this.priority).icon(
				new ResourceLocation(L2Artifacts.MODID, "slot/empty_" + this.getIdentifier() + "_slot"));
	}
}