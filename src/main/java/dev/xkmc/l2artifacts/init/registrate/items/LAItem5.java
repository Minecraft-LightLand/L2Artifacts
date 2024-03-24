package dev.xkmc.l2artifacts.init.registrate.items;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;

public class LAItem5 {


	//v5
	public static final SetEntry<ArtifactSet> SET_CELL;
	public static final SetEntry<ArtifactSet> SET_FLESH;
	public static final SetEntry<ArtifactSet> SET_FUNGUS;
	public static final SetEntry<ArtifactSet> SET_GILDED;
	public static final SetEntry<ArtifactSet> SET_POISONOUS;
	public static final SetEntry<ArtifactSet> SET_SLIMY;
	public static final SetEntry<ArtifactSet> SET_THERMAL;

	static {
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("cell");
			SET_CELL = helper.regSet(1, 5, "Dead Cell")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}


		{
			SetRegHelper helper = REGISTRATE.getSetHelper("flesh");
			SET_FLESH = helper.regSet(1, 5, "Flesh")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("fungus");
			SET_FUNGUS = helper.regSet(1, 5, "Fungus")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("gilded");
			SET_GILDED = helper.regSet(1, 5, "Gilded")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("poisonous");
			SET_POISONOUS = helper.regSet(1, 5, "Poisonous")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("slimy");
			SET_SLIMY = helper.regSet(1, 5, "Slimy")
					.setSlots(SLOT_HEAD, SLOT_BODY, SLOT_BELT).regItems()
					.buildConfig(c -> {
					})
					.register();
		}

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
