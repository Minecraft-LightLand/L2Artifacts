package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.v2.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class LAItem2 {

	// v2
	public static final SetEntry<ArtifactSet> SET_FROZE;
	public static final SetEntry<ArtifactSet> SET_EXECUTOR;
	public static final SetEntry<ArtifactSet> SET_PHYSICAL;
	public static final SetEntry<ArtifactSet> SET_WRATH;
	public static final RegistryEntry<FrozeSlowEffect> EFF_FROZE_SLOW;
	public static final RegistryEntry<FrozeBreakEffect> EFF_FROZE_BREAK;
	public static final RegistryEntry<ExecutorSelfHurtEffect> EFF_EXECUTOR_SELF_HURT;
	public static final RegistryEntry<ExecutorLimitEffect> EFF_EXECUTOR_LIMIT;
	public static final RegistryEntry<PhysicalDamageEffect> EFF_PHYSICAL_DAMAGE;
	public static final RegistryEntry<AttributeSetEffect> EFF_PHYSICAL_ARMOR;
	public static final RegistryEntry<WrathEffect> EFF_WRATH_POISON;
	public static final RegistryEntry<WrathEffect> EFF_WRATH_SLOW;
	public static final RegistryEntry<WrathEffect> EFF_WRATH_FIRE;

	static {

		// v2

		//froze
		{
			LinearFuncEntry damage = REGISTRATE.regLinear("froze_slow_fire_damage", 1.2, 0);
			LinearFuncEntry period = REGISTRATE.regLinear("froze_slow_period", 80, 40);
			LinearFuncEntry level = REGISTRATE.regLinear("froze_slow_level", 0, 1);
			LinearFuncEntry factor = REGISTRATE.regLinear("froze_break", 1.2, 0.1);
			EFF_FROZE_SLOW = REGISTRATE.setEffect("froze_slow", () -> new FrozeSlowEffect(damage, period, level))
					.desc("Frozen Blade",
							"Take %s%% fire damage. When not on fire, apply level %s slow effect on attack target for %s seconds"
					).register();
			EFF_FROZE_BREAK = REGISTRATE.setEffect("froze_break", () -> new FrozeBreakEffect(factor))
					.desc("Ice Breaker",
							"Attacks targetting slowed enemy will have %s%% more damage"
					).register();

			SET_FROZE = Wrappers.cast(REGISTRATE.regSet("froze", ArtifactSet::new, 1, 5, "Frozen Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem2.EFF_FROZE_SLOW.get())
							.add(5, LAItem2.EFF_FROZE_BREAK.get()))
					.register());
		}

		//executor
		{
			LinearFuncEntry atk = REGISTRATE.regLinear("executor_attack", 0.3, 0.15);
			LinearFuncEntry hurt = REGISTRATE.regLinear("executor_self_hurt", 0.2, 0);
			LinearFuncEntry factor = REGISTRATE.regLinear("executor_limit", 0.3, -0.05);

			EFF_EXECUTOR_SELF_HURT = REGISTRATE.setEffect("executor_self_hurt", () -> new ExecutorSelfHurtEffect(
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, atk, true),
					hurt)).desc("Brutal Execution",
					"When kill enemies, deal real damage to oneself equal to %s%% of enemies' max health."
			).register();
			EFF_EXECUTOR_LIMIT = REGISTRATE.setEffect("executor_limit", () -> new ExecutorLimitEffect(factor))
					.desc("Cold Hearted",
							"The damage dealt to oneself will be capped to one's max health, and then reduced to %s%%"
					).register();

			SET_EXECUTOR = Wrappers.cast(REGISTRATE.regSet("executor", ArtifactSet::new, 1, 5, "Executor Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem2.EFF_EXECUTOR_SELF_HURT.get())
							.add(5, LAItem2.EFF_EXECUTOR_LIMIT.get()))
					.register());
		}

		// physical
		{
			LinearFuncEntry atk = REGISTRATE.regLinear("physical_attack", 0.2, 0.1);
			LinearFuncEntry armor = REGISTRATE.regLinear("physical_armor", 4, 2);
			LinearFuncEntry factor = REGISTRATE.regLinear("physical_reduce_magic", 0.5, 0);

			EFF_PHYSICAL_DAMAGE = REGISTRATE.setEffect("physical_damage", () -> new PhysicalDamageEffect(
					new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE,
							AttributeModifier.Operation.MULTIPLY_BASE, atk, true),
					factor)).desc("Barbaric Attack",
					"Magical damage dealt will be reduced to %s%%"
			).register();
			EFF_PHYSICAL_ARMOR = REGISTRATE.setEffect("physical_armor", () -> new AttributeSetEffect(
					new AttrSetEntry(() -> Attributes.ARMOR,
							ADDITION, armor, false)
			)).lang("Survival Instinct").register();

			SET_PHYSICAL = Wrappers.cast(REGISTRATE.regSet("physical", ArtifactSet::new, 1, 5, "Courage Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem2.EFF_PHYSICAL_DAMAGE.get())
							.add(5, LAItem2.EFF_PHYSICAL_ARMOR.get()))
					.register());
		}

		// wrath
		{
			LinearFuncEntry dec = REGISTRATE.regLinear("wrath_decrease", 0.8, 0);
			LinearFuncEntry inc = REGISTRATE.regLinear("wrath_increase", 1.2, 0.1);
			EFF_WRATH_POISON = REGISTRATE.setEffect("wrath_poison", () -> new WrathEffect(e -> e.hasEffect(MobEffects.POISON), dec, inc))
					.desc("Bad Day Encounters Bad Luck",
							"When target is poisoned, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();
			EFF_WRATH_SLOW = REGISTRATE.setEffect("wrath_slow", () -> new WrathEffect(e -> e.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), dec, inc))
					.desc("Snow Storm Encounters Blitz Winter",
							"When target is slowed, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();
			EFF_WRATH_FIRE = REGISTRATE.setEffect("wrath_fire", () -> new WrathEffect(Entity::isOnFire, dec, inc))
					.desc("Emergency Encounters Unwanted Fight",
							"When target is on fire, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();

			SET_WRATH = Wrappers.cast(REGISTRATE.regSet("wrath", ArtifactSet::new, 1, 5, "Curse of Bad Luck")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, LAItem2.EFF_WRATH_POISON.get())
							.add(3, LAItem2.EFF_WRATH_SLOW.get())
							.add(5, LAItem2.EFF_WRATH_FIRE.get()))
					.register());
		}

	}

	public static void register() {

	}

}
