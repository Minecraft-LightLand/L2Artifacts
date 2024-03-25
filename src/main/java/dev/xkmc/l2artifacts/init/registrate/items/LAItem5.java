package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.v5.*;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class LAItem5 {


	//v5
	public static final SetEntry<ArtifactSet> SET_CELL;
	public static final SetEntry<ArtifactSet> SET_FLESH;
	public static final SetEntry<ArtifactSet> SET_FUNGUS;
	public static final SetEntry<ArtifactSet> SET_GILDED;
	public static final SetEntry<ArtifactSet> SET_POISONOUS;
	public static final SetEntry<ArtifactSet> SET_SLIMY;
	public static final SetEntry<ArtifactSet> SET_THERMAL;

	public static final RegistryEntry<FungusInfect> FUNGUS_3;
	public static final RegistryEntry<FungusExplode> FUNGUS_5;
	public static final RegistryEntry<AttributeSetEffect> GILDED_3;
	public static final RegistryEntry<GildedAttack> GILDED_5;
	public static final RegistryEntry<PoisonTouch> POISON_2;
	public static final RegistryEntry<PoisonAttack> POISON_5;
	public static final RegistryEntry<ThermalMotive> THERMAL_2;
	public static final RegistryEntry<ThermalShield> THERMAL_4;


	static {
		// cell TODO
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("cell");
			SET_CELL = helper.regSet(1, 5, "Dead Cell")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		// flesh TODO
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("flesh");
			SET_FLESH = helper.regSet(1, 5, "Flesh")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		// fungus
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("fungus");
			var duration = helper.regLinear("infect_duration", 200, 40);
			var range = helper.regLinear("explode_range", 6, 1);
			var rate = helper.regLinear("explode_rate", 0.6, 0.3);
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
					new AttrSetEntry(() -> Attributes.ARMOR, MULTIPLY_BASE, armor, true),
					new AttrSetEntry(() -> Attributes.ARMOR_TOUGHNESS, MULTIPLY_BASE, tough, true)
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
			var chance = helper.regLinear("chance", 0.1, 0.05);
			var duration = helper.regLinear("duration", 100, 40);
			var attack = helper.regLinear("attack", 0.1, 0.05);
			POISON_2 = helper.setEffect("poisonous_touch", () -> new PoisonTouch(chance, duration))
					.desc("Poisonous Touch", "On hit, %s%% chance to inflict target with %s, %s, and %s")
					.register();
			POISON_5 = helper.setEffect("poisonous_erosion", () -> new PoisonAttack(attack))
					.desc("Poisonous Erosion", "For every hramful effect on target, increase damage you dealt by %s%%")
					.register();
			SET_POISONOUS = helper.regSet(1, 5, "Poisonous")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(2, POISON_2.get()).add(5, POISON_5.get())).register();
		}

		// slimy TODO
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("slimy");
			SET_SLIMY = helper.regSet(1, 5, "Slimy")
					.setSlots(SLOT_HEAD, SLOT_BODY, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		// thermal
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("thermal");

			var atk = helper.regLinear("thermal_motive", 0.2, 0.1);
			var def = helper.regLinear("forst_shield", 0.2, 0.1);
			var dura = helper.regLinear("duration", 60, 0);

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
