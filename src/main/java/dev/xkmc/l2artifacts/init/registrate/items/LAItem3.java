package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.SimpleCASetEffect;
import dev.xkmc.l2artifacts.content.effects.v3.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class LAItem3 {

	// v3
	public static final SetEntry<ArtifactSet> SET_PHOTOSYN;
	public static final SetEntry<ArtifactSet> SET_VAMPIRE;
	public static final SetEntry<ArtifactSet> SET_SUN_BLOCK;
	public static final SetEntry<ArtifactSet> SET_GLUTTONY;
	public static final SetEntry<ArtifactSet> SET_FALLEN;
	public static final RegistryEntry<Photosynthesisffect> EFF_PHOTOSYN;
	public static final RegistryEntry<VampireBurn> EFF_VAMPIRE_BURN;
	public static final RegistryEntry<VampireHeal> EFF_VAMPIRE_HEAL;
	public static final RegistryEntry<SunBlockMask> EFF_SUN_BLOCK;
	public static final RegistryEntry<SimpleCASetEffect> EFF_GLUTTONY_FAST;
	public static final RegistryEntry<GluttonyHeal> EFF_GLUTTONY_HEAL;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_1;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_2;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_3;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_4;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_5;

	static {
		// v3

		//photosynthesis
		{
			LinearFuncEntry period = REGISTRATE.regLinear("photosynthesis_period", 5, -1);
			LinearFuncEntry lo = REGISTRATE.regLinear("photosynthesis_low", 5, -1);
			LinearFuncEntry hi = REGISTRATE.regLinear("photosynthesis_high", 12, -1);
			EFF_PHOTOSYN = REGISTRATE.setEffect("photosynthesis", () -> new Photosynthesisffect(period, lo, hi))
					.desc("Flourishing Ring",
							"Every %s seconds, when under sun with light level of %s or higher, restore food level. When in light level lower than %s, increase exhaustion"
					).register();

			SET_PHOTOSYN = Wrappers.cast(REGISTRATE.regSet("photosynthesis", ArtifactSet::new, 1, 5, "Photosynthesis Hat")
					.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
							.add(1, LAItem3.EFF_PHOTOSYN.get()))
					.register());
		}

		// vampire
		{
			LinearFuncEntry lo = REGISTRATE.regLinear("vampire_burn_low", 3, 1);
			LinearFuncEntry light = REGISTRATE.regLinear("vampire_heal_light", 4, 1);
			LinearFuncEntry amplify = REGISTRATE.regLinear("vampire_heal_ratio", 0.2, 0.1);
			EFF_VAMPIRE_BURN = REGISTRATE.setEffect("vampire_burn", () -> new VampireBurn(lo))
					.desc("Photophobic",
							"When under direct sunlight, burn. " +
									"Whe sunlight light level received is not higher than %s, get Night Vision."
					).register();
			EFF_VAMPIRE_HEAL = REGISTRATE.setEffect("vampire_heal", () -> new VampireHeal(light, amplify))
					.desc("Blood Thurst",
							"When sunlight light level received is not higher than %s, " +
									"when dealing damage, heal %s%% of that damage."
					).register();

			SET_VAMPIRE = Wrappers.cast(REGISTRATE.regSet("vampire", ArtifactSet::new, 1, 5, "Vampire Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, LAItem3.EFF_VAMPIRE_BURN.get())
							.add(4, LAItem3.EFF_VAMPIRE_HEAL.get()))
					.register());
		}

		// sun block
		{
			EFF_SUN_BLOCK = REGISTRATE.setEffect("sun_block", SunBlockMask::new)
					.desc("Sunlight Hat", "Block sunlight for the player").register();

			SET_SUN_BLOCK = Wrappers.cast(REGISTRATE.regSet("sun_block", ArtifactSet::new, 1, 5, "Umbrella")
					.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
							.add(1, LAItem3.EFF_SUN_BLOCK.get()))
					.register());
		}

		// gluttony
		{
			LinearFuncEntry atk = REGISTRATE.regLinear("gluttony_attack", 0.2, 0.1);
			LinearFuncEntry swi = REGISTRATE.regLinear("gluttony_swing", 0.04, 0.02);
			LinearFuncEntry spe = REGISTRATE.regLinear("gluttony_speed", 0.1, 0.05);
			EFF_GLUTTONY_FAST = REGISTRATE.setEffect("gluttony_fast", () -> new SimpleCASetEffect(
					player -> player.hasEffect(MobEffects.HUNGER),
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk, true),
					new AttrSetEntry(() -> Attributes.ATTACK_SPEED, MULTIPLY_BASE, swi, true),
					new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, spe, true)
			)).desc("Hunger Strike", "When having Hunger effect:").register();

			LinearFuncEntry eat = REGISTRATE.regLinear("gluttony_eat", 2, 1);
			EFF_GLUTTONY_HEAL = REGISTRATE.setEffect("gluttony_eat", () -> new GluttonyHeal(eat))
					.desc("Flesh Eater", "When kill entities, restore food level by %s and saturation by %s")
					.register();

			SET_GLUTTONY = Wrappers.cast(REGISTRATE.regSet("gluttony", ArtifactSet::new, 1, 5, "Gluttony Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem3.EFF_GLUTTONY_FAST.get())
							.add(5, LAItem3.EFF_GLUTTONY_HEAL.get()))
					.register());
		}

		// fallen
		{
			LinearFuncEntry atk1 = REGISTRATE.regLinear("fury_of_fallen_1", 0.1, 0.05);
			LinearFuncEntry atk2 = REGISTRATE.regLinear("fury_of_fallen_2", 0.2, 0.10);
			LinearFuncEntry atk3 = REGISTRATE.regLinear("fury_of_fallen_3", 0.3, 0.15);
			LinearFuncEntry atk4 = REGISTRATE.regLinear("fury_of_fallen_4", 0.4, 0.20);
			LinearFuncEntry atk5 = REGISTRATE.regLinear("fury_of_fallen_5", 0.5, 0.25);
			EFF_FALLEN_1 = REGISTRATE.setEffect("fury_of_fallen_1", () -> new SimpleCASetEffect(
					player -> player.getHealth() <= player.getMaxHealth() * 0.5,
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk1, true)
			)).desc("Furry of Fallen Lv.1", "When health is less than 50%:").register();
			EFF_FALLEN_2 = REGISTRATE.setEffect("fury_of_fallen_2", () -> new SimpleCASetEffect(
					player -> player.getHealth() <= player.getMaxHealth() * 0.4,
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk2, true)
			)).desc("Furry of Fallen Lv.2", "When health is less than 40%:").register();
			EFF_FALLEN_3 = REGISTRATE.setEffect("fury_of_fallen_3", () -> new SimpleCASetEffect(
					player -> player.getHealth() <= player.getMaxHealth() * 0.3,
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk3, true)
			)).desc("Furry of Fallen Lv.3", "When health is less than 30%:").register();
			EFF_FALLEN_4 = REGISTRATE.setEffect("fury_of_fallen_4", () -> new SimpleCASetEffect(
					player -> player.getHealth() <= player.getMaxHealth() * 0.2,
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk4, true)
			)).desc("Furry of Fallen Lv.4", "When health is less than 20%:").register();
			EFF_FALLEN_5 = REGISTRATE.setEffect("fury_of_fallen_5", () -> new SimpleCASetEffect(
					player -> player.getHealth() <= player.getMaxHealth() * 0.1,
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk5, true)
			)).desc("Furry of Fallen Lv.5", "When health is less than 10%:").register();

			SET_FALLEN = Wrappers.cast(REGISTRATE.regSet("fury_of_fallen", ArtifactSet::new, 1, 5, "Fury of Fallen")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, LAItem3.EFF_FALLEN_1.get())
							.add(2, LAItem3.EFF_FALLEN_2.get())
							.add(3, LAItem3.EFF_FALLEN_3.get())
							.add(4, LAItem3.EFF_FALLEN_4.get())
							.add(5, LAItem3.EFF_FALLEN_5.get()))
					.register());
		}

	}

	public static void register() {

	}

}
