package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearParam;
import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.LinearFunc;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.content.search.token.PlayerFilterCap;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootModifier;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.datapack.DatapackReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

public class ArtifactTypeRegistry {

	public static final L2Registrate.RegistryInstance<ArtifactSlot> SLOT = REGISTRATE.newRegistry("slot", ArtifactSlot.class);
	public static final L2Registrate.RegistryInstance<ArtifactSet> SET = REGISTRATE.newRegistry("set", ArtifactSet.class);
	public static final L2Registrate.RegistryInstance<SetEffect> SET_EFFECT = REGISTRATE.newRegistry("set_effect", SetEffect.class);
	public static final L2Registrate.RegistryInstance<LinearFunc> LINEAR = REGISTRATE.newRegistry("linear", LinearFunc.class);

	public static final DatapackReg<StatType> STAT_TYPE = L2Artifacts.REG.dataReg("stat_type", StatType.class);

	public static final DataMapReg<LinearFunc, LinearParam> LINEAR_CONFIG = L2Artifacts.REG.dataMap("linear", LINEAR.key(), LinearParam.class);
	public static final DataMapReg<ArtifactSet, ArtifactSetConfig> ARTIFACT_SETS = L2Artifacts.REG.dataMap("artifact_sets", SET.key(), ArtifactSetConfig.class);

	public static final SimpleEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", () -> new ArtifactSlot(ArtifactSlotCuriosType.HEAD));
	public static final SimpleEntry<ArtifactSlot> SLOT_NECKLACE = regSlot("necklace", () -> new ArtifactSlot(ArtifactSlotCuriosType.NECKLACE));
	public static final SimpleEntry<ArtifactSlot> SLOT_BRACELET = regSlot("bracelet", () -> new ArtifactSlot(ArtifactSlotCuriosType.BRACELET));
	public static final SimpleEntry<ArtifactSlot> SLOT_BODY = regSlot("body", () -> new ArtifactSlot(ArtifactSlotCuriosType.BODY));
	public static final SimpleEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", () -> new ArtifactSlot(ArtifactSlotCuriosType.BELT));

	private static final AttReg ATT = AttReg.of(L2Artifacts.REG);
	public static final AttVal.PlayerVal<PlayerFilterCap> FILTER = ATT.player("filter",
			PlayerFilterCap.class, PlayerFilterCap::new, PlayerCapabilityNetworkHandler::new);

	private static final CdcReg<IGlobalLootModifier> CDC = CdcReg.of(L2Artifacts.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
	public static final CdcVal<ArtifactLootModifier> SER = CDC.reg("main", ArtifactLootModifier.CODEC);


	public static void register() {

	}

	private static SimpleEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
		return new SimpleEntry<>(REGISTRATE.generic(SLOT, id, slot).defaultLang().register());
	}

}
