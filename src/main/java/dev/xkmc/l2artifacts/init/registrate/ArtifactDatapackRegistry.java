package dev.xkmc.l2artifacts.init.registrate;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.datapack.DatapackEntry;
import dev.xkmc.l2library.base.datapack.DatapackInstance;
import dev.xkmc.l2library.base.datapack.EntrySetBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.function.Supplier;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class ArtifactDatapackRegistry {

	public static final DatapackInstance<ArtifactStatType, ArtifactStatTypeHolder> STAT_TYPE =
			L2Artifacts.REGISTRATE.datapackRegistry("stat_type", ArtifactStatTypeHolder.class,
					ArtifactStatTypeHolder::new, ArtifactStatType.CODEC);

	public static final EntrySetBuilder<ArtifactStatType, ArtifactStatTypeHolder> ENTRIES = STAT_TYPE.startGen(L2Artifacts.REGISTRATE);

	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> HEALTH_ADD = regStat("health_add", () -> Attributes.MAX_HEALTH, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ARMOR_ADD = regStat("armor_add", () -> Attributes.ARMOR, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> TOUGH_ADD = regStat("tough_add", () -> Attributes.ARMOR_TOUGHNESS, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_ADD = regStat("attack_add", () -> Attributes.ATTACK_DAMAGE, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> REACH_ADD = regStat("reach_add", ForgeMod.ENTITY_REACH, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> CR_ADD = regStat("crit_rate_add", L2DamageTracker.CRIT_RATE, ADDITION, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> CD_ADD = regStat("crit_damage_add", L2DamageTracker.CRIT_DMG, ADDITION, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_MULT = regStat("attack_mult", () -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> SPEED_MULT = regStat("speed_mult", () -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_SPEED_MULT = regStat("attack_speed_mult", () -> Attributes.ATTACK_SPEED, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> BOW_ADD = regStat("bow_strength_add", L2DamageTracker.BOW_STRENGTH, ADDITION, true);

	public static void register() {

	}

	private static DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> regStat(String id, Supplier<Attribute> attr, AttributeModifier.Operation op, boolean usePercent) {
		return ENTRIES.reg(new ResourceLocation(L2Artifacts.MODID, id), () -> new ArtifactStatType(attr.get(), op.name(), usePercent), RegistrateLangProvider.toEnglishName(id));
	}

}


