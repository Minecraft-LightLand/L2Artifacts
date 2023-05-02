package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static dev.xkmc.l2library.init.L2Library.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class LAItem0 {

	// bland
	public static final SetEntry<ArtifactSet> SET_GAMBLER;
	public static final SetEntry<ArtifactSet> SET_BERSERKER;
	public static final SetEntry<ArtifactSet> SET_ARCHER;
	public static final RegistryEntry<AttributeSetEffect> EFF_GAMBLER_3;
	public static final RegistryEntry<AttributeSetEffect> EFF_GAMBLER_5;
	public static final RegistryEntry<AttributeSetEffect> EFF_BERSERKER_3;
	public static final RegistryEntry<AttributeSetEffect> EFF_BERSERKER_5;
	public static final RegistryEntry<AttributeSetEffect> EFF_ARCHER_3;
	public static final RegistryEntry<AttributeSetEffect> EFF_ARCHER_5;

	static {
		// gambler set
		{
			LinearFuncEntry cr3 = REGISTRATE.regLinear("gambler_3_crit_rate", -0.2, 0);
			LinearFuncEntry cd3 = REGISTRATE.regLinear("gambler_3_crit_damage", 0.2, 0.1);
			LinearFuncEntry cr5 = REGISTRATE.regLinear("gambler_5_crit_rate", 0.04, 0.02);
			LinearFuncEntry cd5 = REGISTRATE.regLinear("gambler_5_crit_damage", 0.08, 0.04);
			LinearFuncEntry luck = REGISTRATE.regLinear("gambler_5_luck", 1, 0.5);

			EFF_GAMBLER_3 = REGISTRATE.setEffect("gambler_3", () -> new AttributeSetEffect(
					new AttrSetEntry(CRIT_RATE, ADDITION, cr3, true),
					new AttrSetEntry(CRIT_DMG, ADDITION, cd3, true)
			)).lang("Pursuit of Bets").register();

			EFF_GAMBLER_5 = REGISTRATE.setEffect("gambler_5", () -> new AttributeSetEffect(
					new AttrSetEntry(CRIT_RATE, ADDITION, cr5, true),
					new AttrSetEntry(CRIT_DMG, ADDITION, cd5, true),
					new AttrSetEntry(() -> Attributes.LUCK, ADDITION, luck, false)
			)).lang("Bless of Luck").register();

			SET_GAMBLER = Wrappers.cast(REGISTRATE.regSet("gambler", ArtifactSet::new, 1, 5, "Gambler Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem0.EFF_GAMBLER_3.get())
							.add(5, LAItem0.EFF_GAMBLER_5.get()))
					.register());
		}

		// berserker set
		{
			LinearFuncEntry ar3 = REGISTRATE.regLinear("berserker_3_armor", -10, 0);
			LinearFuncEntry atk3 = REGISTRATE.regLinear("berserker_3_attack", 0.2, 0.1);
			LinearFuncEntry speed = REGISTRATE.regLinear("berserker_5_speed", 0.04, 0.02);
			LinearFuncEntry haste = REGISTRATE.regLinear("berserker_5_attack_speed", 0.04, 0.02);
			LinearFuncEntry cd5 = REGISTRATE.regLinear("berserker_5_crit_damage", 0.04, 0.02);

			EFF_BERSERKER_3 = REGISTRATE.setEffect("berserker_3", () -> new AttributeSetEffect(
					new AttrSetEntry(() -> Attributes.ARMOR, ADDITION, ar3, false),
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk3, true)
			)).lang("Unpolished Bruteforce").register();

			EFF_BERSERKER_5 = REGISTRATE.setEffect("berserker_5", () -> new AttributeSetEffect(
					new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, speed, true),
					new AttrSetEntry(() -> Attributes.ATTACK_SPEED, MULTIPLY_BASE, haste, true),
					new AttrSetEntry(CRIT_DMG, ADDITION, cd5, true)
			)).lang("Subconscious Fight").register();

			SET_BERSERKER = Wrappers.cast(REGISTRATE.regSet("berserker", ArtifactSet::new, 1, 5, "Berserker Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem0.EFF_BERSERKER_3.get())
							.add(5, LAItem0.EFF_BERSERKER_5.get()))
					.register());
		}

		// archer set
		{

			LinearFuncEntry atk3 = REGISTRATE.regLinear("archer_3_attack", -0.4, 0);
			LinearFuncEntry bow3 = REGISTRATE.regLinear("archer_3_bow", 0.4, 0.2);
			LinearFuncEntry haste = REGISTRATE.regLinear("archer_5_attack_speed", -0.4, 0);
			LinearFuncEntry cr5 = REGISTRATE.regLinear("archer_5_crit_rate", 0.4, 0.2);

			EFF_ARCHER_3 = REGISTRATE.setEffect("archer_3", () -> new AttributeSetEffect(
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk3, true),
					new AttrSetEntry(BOW_STRENGTH, ADDITION, bow3, true)
			)).lang("Specialty of Archer").register();

			EFF_ARCHER_5 = REGISTRATE.setEffect("archer_5", () -> new AttributeSetEffect(
					new AttrSetEntry(() -> Attributes.ATTACK_SPEED, MULTIPLY_BASE, haste, true),
					new AttrSetEntry(CRIT_RATE, ADDITION, cr5, true)
			)).lang("Focus of Archer").register();

			SET_ARCHER = Wrappers.cast(REGISTRATE.regSet("archer", ArtifactSet::new, 1, 5, "Archer Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem0.EFF_ARCHER_3.get())
							.add(5, LAItem0.EFF_ARCHER_5.get()))
					.register());
		}
	}

	public static void register() {

	}

}
