package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.*;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL;

@SuppressWarnings({"raw_type", "unchecked"})
public class ArtifactItemRegistry {

	public static final Tab TAB_ARTIFACT = new Tab("artifacts");

	static {
		REGISTRATE.creativeModeTab(() -> TAB_ARTIFACT);
	}

	public static final ItemEntry<ExpItem>[] RANKED_ITEMS;

	static {
		int n = 5;
		RANKED_ITEMS = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			RANKED_ITEMS[i] = REGISTRATE.item("artifact_experience_" + r, p -> new ExpItem(p, r))
					.defaultLang().register();
		}
	}

	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_GAMBLER = REGISTRATE.regSet("gambler", ArtifactSet::new, 1, 5, SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT);
	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_BERSERKER = REGISTRATE.regSet("berserker", ArtifactSet::new, 1, 5, SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT);
	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_SAINT = REGISTRATE.regSet("saint", ArtifactSet::new, 1, 5, SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT);

	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_PERFECTION = REGISTRATE.regSet("perfection", ArtifactSet::new, 1, 5, SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT);

	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_DAMOCLES = REGISTRATE.regSet("damocles", ArtifactSet::new, 1, 5, SLOT_HEAD);
	public static final ArtifactRegistrate.SetEntry<ArtifactSet> SET_PROTECTION = REGISTRATE.regSet("protection", ArtifactSet::new, 1, 5, SLOT_HEAD);

	public static final RegistryEntry<AttributeSetEffect> EFF_GAMBLER_3 = REGISTRATE.setEffect("gambler_3", () -> new AttributeSetEffect(
			new AttributeSetEffect.AttrSetEntry(CRIT_RATE, ADDITION, -0.2, 0, true),
			new AttributeSetEffect.AttrSetEntry(CRIT_DMG, ADDITION, 0.2, 0.2, true)
	));

	public static final RegistryEntry<AttributeSetEffect> EFF_GAMBLER_5 = REGISTRATE.setEffect("gambler_5", () -> new AttributeSetEffect(
			new AttributeSetEffect.AttrSetEntry(CRIT_RATE, ADDITION, 0.04, 0.02, true),
			new AttributeSetEffect.AttrSetEntry(CRIT_DMG, ADDITION, 0.08, 0.04, true),
			new AttributeSetEffect.AttrSetEntry(() -> Attributes.LUCK, ADDITION, 1, 0.5, false)
	));

	public static final RegistryEntry<AttributeSetEffect> EFF_BERSERKER_3 = REGISTRATE.setEffect("berserker_3", () -> new AttributeSetEffect(
			new AttributeSetEffect.AttrSetEntry(() -> Attributes.ARMOR, ADDITION, -10, 0, false),
			new AttributeSetEffect.AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_TOTAL, 0.2, 0.1, true)
	));

	public static final RegistryEntry<AttributeSetEffect> EFF_BERSERKER_5 = REGISTRATE.setEffect("berserker_5", () -> new AttributeSetEffect(
			new AttributeSetEffect.AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, MULTIPLY_TOTAL, 0.04, 0.02, true),
			new AttributeSetEffect.AttrSetEntry(() -> Attributes.ATTACK_SPEED, MULTIPLY_TOTAL, 0.04, 0.02, true),
			new AttributeSetEffect.AttrSetEntry(CRIT_DMG, ADDITION, 0.04, 0.02, true)
	));

	public static final RegistryEntry<PerfectionAbsorption> EFF_PERFECTION_ABSORPTION = REGISTRATE.setEffect(
			"perfection_absorption", () -> new PerfectionAbsorption(4, 2));

	public static final RegistryEntry<PerfectionProtection> EFF_PERFECTION_PROTECTION = REGISTRATE.setEffect(
			"perfection_protection", () -> new PerfectionProtection(0.2, 0.1));

	public static final RegistryEntry<DamoclesSword> EFF_DAMOCLES = REGISTRATE.setEffect(
			"damocles", () -> new DamoclesSword(1, 0.5));

	public static final RegistryEntry<SaintReduction> EFF_SAINT_REDUCTION = REGISTRATE.setEffect(
			"saint_reduction", () -> new SaintReduction(0.25, 0.05));

	public static final RegistryEntry<SaintRestoration> EFF_SAINT_RESTORATION = REGISTRATE.setEffect(
			"saint_restoration", () -> new SaintRestoration(100, 10));

	public static final RegistryEntry<ProtectionResistance> EFF_PROTECTION_RESISTANCE = REGISTRATE.setEffect(
			"protection_resistance", ProtectionResistance::new);

	public static void register() {

	}

	public static class Tab extends CreativeModeTab {

		public Tab(String label) {
			super(L2Artifacts.MODID + "." + label);
		}

		@Override
		public ItemStack makeIcon() {
			return SET_GAMBLER.items[0][0].asStack();
		}
	}

}
