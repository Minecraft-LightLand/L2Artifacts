package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.v5.FungusExplode;
import dev.xkmc.l2artifacts.content.effects.v5.FungusInfect;
import dev.xkmc.l2artifacts.content.effects.v5.GildedAttack;
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
					.buildConfig(c -> c
							.add(3, GILDED_3.get())
							.add(5, GILDED_5.get())
					).register();
		}

		// poisonous TODO
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("poisonous");
			SET_POISONOUS = helper.regSet(1, 5, "Poisonous")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
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

		// thermal TODO
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("thermal");
			SET_THERMAL = helper.regSet(1, 5, "Thermal")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

	}

	public static void register() {

	}

}
