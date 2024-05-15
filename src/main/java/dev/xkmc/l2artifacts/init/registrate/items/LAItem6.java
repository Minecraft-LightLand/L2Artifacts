package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static dev.xkmc.l2damagetracker.init.L2DamageTracker.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;

public class LAItem6 {

	public static final SetEntry<ArtifactSet> SET_MAGE;
	public static final SetEntry<ArtifactSet> SET_PIRATE;

	public static final RegistryEntry<AttributeSetEffect> EFF_MAGE_2;
	public static final RegistryEntry<AttributeSetEffect> EFF_MAGE_4;
	public static final RegistryEntry<AttributeSetEffect> EFF_PIRATE_2;
	public static final RegistryEntry<AttributeSetEffect> EFF_PIRATE_4;

	static {

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("mage");
			LinearFuncEntry m2 = helper.regLinear("mage_2_magic", 0.2, 0.1);
			LinearFuncEntry r4 = helper.regLinear("mage_4_reduction", 2, 1);

			EFF_MAGE_2 = helper.setEffect("mage_2", () -> new AttributeSetEffect(
					new AttrSetEntry(MAGIC_FACTOR::get, ADDITION, m2, true)
			)).lang("Magical Strength").register();

			EFF_MAGE_4 = helper.setEffect("mage_4", () -> new AttributeSetEffect(
					new AttrSetEntry(ABSORB::get, ADDITION, r4, false)
			)).lang("Magical Shield").register();

			SET_MAGE = helper.regSet(1, 5, "Mage")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(2, EFF_MAGE_2.get()).add(4, EFF_MAGE_4.get()))
					.register();
		}

		{
			SetRegHelper helper = REGISTRATE.getSetHelper("pirate");
			LinearFuncEntry e2 = helper.regLinear("pirate_2_explosion", 0.2, 0.1);
			LinearFuncEntry e4 = helper.regLinear("pirate_4_explosion", 0.4, 0.2);
			LinearFuncEntry m4 = helper.regLinear("pirate_4_magic", -0.4, 0);

			EFF_PIRATE_2 = helper.setEffect("pirate_2", () -> new AttributeSetEffect(
					new AttrSetEntry(EXPLOSION_FACTOR::get, ADDITION, e2, true)
			)).lang("Pirate Attack!").register();

			EFF_PIRATE_4 = helper.setEffect("pirate_4", () -> new AttributeSetEffect(
					new AttrSetEntry(EXPLOSION_FACTOR::get, ADDITION, e4, true),
					new AttrSetEntry(MAGIC_FACTOR::get, ADDITION, m4, true)
			)).lang("Secret Bomb").register();

			SET_PIRATE = helper.regSet(1, 5, "Pirate")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig(c -> c.add(2, EFF_PIRATE_2.get()).add(4, EFF_PIRATE_4.get()))
					.register();
		}
	}

	public static void register() {

	}

}
