package dev.xkmc.l2artifacts.init.registrate.items;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.v5.*;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEffectEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;

public class LAItem5 {


	//v5
	public static final SetEntry<ArtifactSet> SET_CELL;
	public static final SetEntry<ArtifactSet> SET_FLESH;
	public static final SetEntry<ArtifactSet> SET_FUNGUS;
	public static final SetEntry<ArtifactSet> SET_GILDED;
	public static final SetEntry<ArtifactSet> SET_POISONOUS;
	public static final SetEntry<ArtifactSet> SET_SLIMY;
	public static final SetEntry<ArtifactSet> SET_THERMAL;

	public static final SetEffectEntry<DeadCellDodge> CELL_3;
	public static final SetEffectEntry<DeadCellParry> CELL_5;
	public static final SetEffectEntry<FleshOvergrowth> FLESH_3;
	public static final SetEffectEntry<FleshAttack> FLESH_5;
	public static final SetEffectEntry<FungusInfect> FUNGUS_3;
	public static final SetEffectEntry<FungusExplode> FUNGUS_5;
	public static final SetEffectEntry<AttributeSetEffect> GILDED_3;
	public static final SetEffectEntry<GildedAttack> GILDED_5;
	public static final SetEffectEntry<PoisonTouch> POISON_2;
	public static final SetEffectEntry<PoisonAttack> POISON_5;
	public static final SetEffectEntry<SlimyBuffer> SLIMY_1;
	public static final SetEffectEntry<Slimification> SLIMY_3;
	public static final SetEffectEntry<ThermalMotive> THERMAL_2;
	public static final SetEffectEntry<ThermalShield> THERMAL_4;


	static {
		// cell
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("cell");
			var chance = helper.regLinear("cell_dodge_chance", 0.1, 0.05);
			var reflect = helper.regLinear("cell_parry_reflect", 0.4, 0.2);
			CELL_3 = helper.setEffect("dead_cell_dodge", () -> new DeadCellDodge(chance))
					.desc("Dead Cells Dodge", "When you are sneaking, you have %s%% chance to dodge a melee / projectile damage")
					.register();
			CELL_5 = helper.setEffect("dead_cell_parry", () -> new DeadCellParry(reflect))
					.desc("Dead Cells Parry", "When you shields a melee damage, reflect %s%% of the damage")
					.register();
			SET_CELL = helper.regSet(1, 5, "Dead Cell")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(3, CELL_3.get()).add(5, CELL_5.get()))
					.register();
		}

		// flesh
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("flesh");
			var dur = helper.regLinear("flesh_duration", 100, 20);
			var thr = helper.regLinear("flesh_threshold", 0.5, 0);
			var atk = helper.regLinear("flesh_attack", 0.4, 0.2);
			FLESH_3 = helper.setEffect("flesh_overgrowth", () -> new FleshOvergrowth(dur))
					.desc("Flesh Overgrowth", "On hit, inflict target with %s").register();
			FLESH_5 = helper.setEffect("flesh_decay", () -> new FleshAttack(thr, atk))
					.desc("Flesh Decay", "Damage to target with %s%% or less health is increased by %s%%").register();
			SET_FLESH = helper.regSet(1, 5, "Flesh")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(3, FLESH_3.get()).add(5, FLESH_5.get()))
					.register();
		}

		// fungus
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("fungus");
			var duration = helper.regLinear("fungus_infect_duration", 200, 40);
			var range = helper.regLinear("fungus_explode_range", 6, 1);
			var rate = helper.regLinear("fungus_explode_rate", 0.6, 0.3);
			FUNGUS_3 = helper.setEffect("fungus_infect", () -> new FungusInfect(duration))
					.desc("Fungus Infection", "When hurt target, inflict %s")
					.register();
			FUNGUS_5 = helper.setEffect("fungus_explode", () -> new FungusExplode(range, rate))
					.desc("Spore Explosion", "When hurt target, inflict %s%% of the damage dealt to surrounding enemies within %s block with Fungus Infection effect")
					.register();
			SET_FUNGUS = helper.regSet(1, 5, "Fungus")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c
							.add(3, FUNGUS_3.get())
							.add(5, FUNGUS_5.get())
					).register();
		}

		// gilded
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("gilded");
			var armor = helper.regLinear("gilded_armor", 0.2, 0.1);
			var tough = helper.regLinear("gilded_tough", 0.2, 0.1);
			var atk = helper.regLinear("gilded_attack", 0.2, 0.1);
			GILDED_3 = helper.setEffect("gilded_3", () -> new AttributeSetEffect(
					new AttrSetEntry(Attributes.ARMOR, ADD_MULTIPLIED_BASE, armor, true),
					new AttrSetEntry(Attributes.ARMOR_TOUGHNESS, ADD_MULTIPLIED_BASE, tough, true)
			)).lang("Gilded Armor").register();
			GILDED_5 = helper.setEffect("gilded_5", () -> new GildedAttack(atk))
					.desc("Infusion Blade", "Increase your attack damage by %s%% of your armor")
					.register();
			SET_GILDED = helper.regSet(1, 5, "Gilded")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(3, GILDED_3.get()).add(5, GILDED_5.get())).register();
		}

		// poisonous
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("poisonous");
			var chance = helper.regLinear("poisonous_chance", 0.1, 0.05);
			var duration = helper.regLinear("poisonous_duration", 100, 40);
			var attack = helper.regLinear("poisonous_attack", 0.1, 0.05);
			POISON_2 = helper.setEffect("poisonous_touch", () -> new PoisonTouch(chance, duration))
					.desc("Poisonous Touch", "On hit, %s%% chance to inflict target with %s, %s, and %s")
					.register();
			POISON_5 = helper.setEffect("poisonous_erosion", () -> new PoisonAttack(attack))
					.desc("Poisonous Erosion", "For every harmful effect on target, increase damage you dealt by %s%%")
					.register();
			SET_POISONOUS = helper.regSet(1, 5, "Poisonous")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(2, POISON_2.get()).add(5, POISON_5.get())).register();
		}

		// slimy
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("slimy");
			var buffer = helper.regLinear("slimy_buffer", 0.9, 0.02);
			var reduction = helper.regLinear("slimy_protection", 0.2, 0.1);
			var penalty = helper.regLinear("slimy_penalty", 1, 0);
			SLIMY_1 = helper.setEffect("slimy_buffer", () -> new SlimyBuffer(buffer))
					.desc("Slimy Buffer", "Reduce falling / flying into wall damage by %s%%")
					.register();
			SLIMY_3 = helper.setEffect("slimification", () -> new Slimification(reduction, penalty))
					.desc("Slimification", "Reduce melee / projectile damage taken by %s%%, but increase fire / freezing / explosion / magic damage taken by %s%%")
					.register();
			SET_SLIMY = helper.regSet(1, 5, "Slimy")
					.setSlots(SLOT_HEAD, SLOT_BODY, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(1, SLIMY_1.get()).add(3, SLIMY_3.get()))
					.register();
		}

		// thermal
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("thermal");

			var atk = helper.regLinear("thermal_motive", 0.2, 0.1);
			var def = helper.regLinear("frost_shield", 0.2, 0.1);
			var dura = helper.regLinear("thermal_effect_duration", 60, 0);

			THERMAL_2 = helper.setEffect("thermal_motive", () -> new ThermalMotive(atk, dura))
					.desc("Thermal Motive", "When you take fire damage, negates the damage and gains %s%% attack boost")
					.register();

			THERMAL_4 = helper.setEffect("frost_shield", () -> new ThermalShield(def, dura))
					.desc("Frost Shield", "When you take freezing damage, negates the damage and gains %s%% damage reduction")
					.register();

			SET_THERMAL = helper.regSet(1, 5, "Thermal")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(2, THERMAL_2.get()).add(4, THERMAL_4.get())).register();
		}

	}

	public static void register() {

	}

}
