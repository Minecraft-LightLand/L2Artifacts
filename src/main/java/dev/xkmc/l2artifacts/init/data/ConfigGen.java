package dev.xkmc.l2artifacts.init.data;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class ConfigGen {

	public static final ResourceLocation HEALTH_ADD = regLang("health_add", "Health +");
	public static final ResourceLocation ARMOR_ADD = regLang("armor_add", "Armor +");
	public static final ResourceLocation TOUGH_ADD = regLang("tough_add", "Armor Toughness +");
	public static final ResourceLocation ATK_ADD = regLang("attack_add", "Melee Damage +");
	public static final ResourceLocation REACH_ADD = regLang("reach_add", "Attack Range +");
	public static final ResourceLocation CR_ADD = regLang("crit_rate_add", "Crit Rate +");
	public static final ResourceLocation CD_ADD = regLang("crit_damage_add", "Crit Damage +");
	public static final ResourceLocation ATK_MULT = regLang("attack_mult", "Melee Damage +%%");
	public static final ResourceLocation SPEED_MULT = regLang("speed_mult", "Speed +%%");
	public static final ResourceLocation ATK_SPEED_MULT = regLang("attack_speed_mult", "Attack Speed +%%");
	public static final ResourceLocation BOW_ADD = regLang("bow_strength_add", "Bow Strength +%%");
	public static final ResourceLocation EXPLOSION_ADD = regLang("explosion_add", "Explosion Damage +%%");
	public static final ResourceLocation MAGIC_ADD = regLang("magic_add", "Magic Damage +%%");

	private static ResourceLocation regLang(String id, String name) {
		L2Artifacts.REGISTRATE.addRawLang("stat_type." + L2Artifacts.MODID + "." + id, name);
		return L2Artifacts.loc(id);
	}

	public static void genSlotType(BootstrapContext<StatType> map) {
		regStat(map, HEALTH_ADD, Attributes.MAX_HEALTH, ADD_VALUE, 2);
		regStat(map, ARMOR_ADD, Attributes.ARMOR, ADD_VALUE, 2);
		regStat(map, TOUGH_ADD, Attributes.ARMOR_TOUGHNESS, ADD_VALUE, 1);
		regStat(map, ATK_ADD, Attributes.ATTACK_DAMAGE, ADD_VALUE, 2);
		regStat(map, REACH_ADD, Attributes.ENTITY_INTERACTION_RANGE, ADD_VALUE, 0.1);
		regStat(map, CR_ADD, L2DamageTracker.CRIT_RATE, ADD_VALUE, 0.05);
		regStat(map, CD_ADD, L2DamageTracker.CRIT_DMG, ADD_VALUE, 0.1);
		regStat(map, ATK_MULT, Attributes.ATTACK_DAMAGE, ADD_MULTIPLIED_BASE, 0.1);
		regStat(map, SPEED_MULT, Attributes.MOVEMENT_SPEED, ADD_MULTIPLIED_BASE, 0.05);
		regStat(map, ATK_SPEED_MULT, Attributes.ATTACK_SPEED, ADD_MULTIPLIED_BASE, 0.05);
		regStat(map, BOW_ADD, L2DamageTracker.BOW_STRENGTH, ADD_VALUE, 0.1);
		regStat(map, EXPLOSION_ADD, L2DamageTracker.EXPLOSION_FACTOR, ADD_VALUE, 0.2);
		regStat(map, MAGIC_ADD, L2DamageTracker.MAGIC_FACTOR, ADD_VALUE, 0.15);
	}

	private static void regStat(BootstrapContext<StatType> map, ResourceLocation id, Holder<Attribute> attr, AttributeModifier.Operation op, double base) {
		map.register(ResourceKey.create(ArtifactTypeRegistry.STAT_TYPE.key(), id), genEntry(attr, op, base, 0.2, 2, 100, 100));
	}

	private static StatType genEntry(
			Holder<Attribute> attr, AttributeModifier.Operation op,
			double base, double sub, double factor,
			int mainWeight, int subWeight
	) {
		return new StatType(base, 1, factor, sub, sub * factor, sub, sub * factor,
				attr, op, mainWeight, subWeight, null);
	}

	public static void register() {

	}

}