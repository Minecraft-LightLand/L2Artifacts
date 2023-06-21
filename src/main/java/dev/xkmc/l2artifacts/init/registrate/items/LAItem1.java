package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.v1.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import dev.xkmc.l2serial.util.Wrappers;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;

public class LAItem1 {

	// v1
	public static final SetEntry<ArtifactSet> SET_SAINT;
	public static final SetEntry<ArtifactSet> SET_PERFECTION;
	public static final SetEntry<ArtifactSet> SET_DAMOCLES;
	public static final SetEntry<ArtifactSet> SET_PROTECTION;
	public static final RegistryEntry<PerfectionAbsorption> EFF_PERFECTION_ABSORPTION;
	public static final RegistryEntry<PerfectionProtection> EFF_PERFECTION_PROTECTION;
	public static final RegistryEntry<SaintReduction> EFF_SAINT_REDUCTION;
	public static final RegistryEntry<SaintRestoration> EFF_SAINT_RESTORATION;
	public static final RegistryEntry<DamoclesSword> EFF_DAMOCLES;
	public static final RegistryEntry<ProtectionResistance> EFF_PROTECTION_RESISTANCE;


	static {
		// saint
		{

			SetRegHelper helper = REGISTRATE.getSetHelper("saint");
			LinearFuncEntry atk = helper.regLinear("saint_reduction_atk", 0.25, 0);
			LinearFuncEntry def = helper.regLinear("saint_reduction_def", 0.25, 0.05);
			LinearFuncEntry period = helper.regLinear("saint_restoration", 100, -10);

			EFF_SAINT_REDUCTION = helper.setEffect("saint_reduction", () -> new SaintReduction(atk, def))
					.desc("Sympathy of Saint",
							"Direct damage dealt reduce to %s%%, damage taken reduce to %s%%"
					).register();
			EFF_SAINT_RESTORATION = helper.setEffect("saint_restoration", () -> new SaintRestoration(period))
					.desc("Bless of Holiness",
							"When have empty main hand, restore health to oneself or allies every %s seconds."
					).register();

			SET_SAINT = helper.regSet(1, 5, "Saint Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_SAINT_REDUCTION.get())
							.add(5, EFF_SAINT_RESTORATION.get()))
					.register();
		}

		// perfection
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("perfection");
			LinearFuncEntry period = helper.regLinear("perfection_absorption_period", 100, 0);
			LinearFuncEntry max = helper.regLinear("perfection_absorption_max", 4, 2);
			LinearFuncEntry def = helper.regLinear("perfection_protection", 0.2, 0.1);
			EFF_PERFECTION_ABSORPTION = helper.setEffect("perfection_absorption", () -> new PerfectionAbsorption(period, max))
					.desc("Heart of Perfection",
							"When at full health, every %s seconds gain 1 point of absorption, maximum %s points."
					).register();
			EFF_PERFECTION_PROTECTION = helper.setEffect("perfection_protection", () -> new PerfectionProtection(def))
					.desc("Eternity of Perfection",
							"When at full health, reduce damage by %s%%"
					).register();

			SET_PERFECTION = helper.regSet(1, 5, "Perfection Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(2, EFF_PERFECTION_PROTECTION.get())
							.add(4, EFF_PERFECTION_ABSORPTION.get()))
					.register();
		}

		//damocles
		{

			SetRegHelper helper = REGISTRATE.getSetHelper("damocles");
			LinearFuncEntry amplify = helper.regLinear("damocles", 1, 0.5);
			EFF_DAMOCLES = helper.setEffect("damocles", () -> new DamoclesSword(amplify))
					.desc("Sword of Damocles",
							"When at full health, direct attack damage increase by %s%%. When below half health, die immediately."
					).register();

			SET_DAMOCLES = Wrappers.cast(helper.regSet(1, 5, "Sword of Damocles")
					.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
							.add(1, EFF_DAMOCLES.get()))
					.register());
		}

		// protection
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("protection");
			EFF_PROTECTION_RESISTANCE = helper.setEffect("protection_resistance", ProtectionResistance::new)
					.desc("Crown of Never Falling Soldier",
							"Damage taken reduced when health is low."
					).register();

			SET_PROTECTION = helper.regSet(1, 5, "Never Falling Crown")
					.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
							.add(1, EFF_PROTECTION_RESISTANCE.get()))
					.register();
		}

	}

	public static void register() {

	}

}
