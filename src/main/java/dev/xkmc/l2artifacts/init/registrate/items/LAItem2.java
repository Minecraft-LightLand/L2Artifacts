package dev.xkmc.l2artifacts.init.registrate.items;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.v2.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEffectEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class LAItem2 {

	// v2
	public static final SetEntry<ArtifactSet> SET_FROZE;
	public static final SetEntry<ArtifactSet> SET_EXECUTOR;
	public static final SetEntry<ArtifactSet> SET_PHYSICAL;
	public static final SetEntry<ArtifactSet> SET_WRATH;
	public static final SetEffectEntry<FrozeSlowEffect> EFF_FROZE_SLOW;
	public static final SetEffectEntry<FrozeBreakEffect> EFF_FROZE_BREAK;
	public static final SetEffectEntry<ExecutorSelfHurtEffect> EFF_EXECUTOR_SELF_HURT;
	public static final SetEffectEntry<ExecutorLimitEffect> EFF_EXECUTOR_LIMIT;
	public static final SetEffectEntry<PhysicalDamageEffect> EFF_PHYSICAL_DAMAGE;
	public static final SetEffectEntry<AttributeSetEffect> EFF_PHYSICAL_ARMOR;
	public static final SetEffectEntry<WrathEffect> EFF_WRATH_POISON;
	public static final SetEffectEntry<WrathEffect> EFF_WRATH_SLOW;
	public static final SetEffectEntry<WrathEffect> EFF_WRATH_FIRE;

	static {

		// v2

		//froze
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("froze");
			LinearFuncEntry damage = helper.regLinear("froze_slow_fire_damage", 1.2, 0);
			LinearFuncEntry period = helper.regLinear("froze_slow_period", 80, 40);
			LinearFuncEntry level = helper.regLinear("froze_slow_level", 0, 1);
			LinearFuncEntry factor = helper.regLinear("froze_break", 1.2, 0.1);
			EFF_FROZE_SLOW = helper.setEffect("froze_slow", () -> new FrozeSlowEffect(damage, period, level))
					.desc("Frozen Blade",
							"Take %s%% fire damage. When not on fire, apply level %s slow effect on attack target for %s seconds"
					).register();
			EFF_FROZE_BREAK = helper.setEffect("froze_break", () -> new FrozeBreakEffect(factor))
					.desc("Ice Breaker",
							"Attacks targetting slowed enemy will have %s%% more damage"
					).register();

			SET_FROZE = helper.regSet(1, 5, "Frozen Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_FROZE_SLOW.get())
							.add(5, EFF_FROZE_BREAK.get()))
					.register();
		}

		//executor
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("executor");
			LinearFuncEntry atk = helper.regLinear("executor_attack", 0.3, 0.15);
			LinearFuncEntry hurt = helper.regLinear("executor_self_hurt", 0.2, 0);
			LinearFuncEntry factor = helper.regLinear("executor_limit", 0.3, -0.05);

			EFF_EXECUTOR_SELF_HURT = helper.setEffect("executor_self_hurt", () -> new ExecutorSelfHurtEffect(
					new AttrSetEntry(Attributes.ATTACK_DAMAGE, ADD_MULTIPLIED_BASE, atk, true),
					hurt)).desc("Brutal Execution",
					"When kill enemies, deal real damage to oneself equal to %s%% of enemies' max health."
			).register();
			EFF_EXECUTOR_LIMIT = helper.setEffect("executor_limit", () -> new ExecutorLimitEffect(factor))
					.desc("Cold Hearted",
							"The damage dealt to oneself will be capped to one's max health, and then reduced to %s%%"
					).register();

			SET_EXECUTOR = helper.regSet(1, 5, "Executor Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_EXECUTOR_SELF_HURT.get())
							.add(5, EFF_EXECUTOR_LIMIT.get()))
					.register();
		}

		// physical
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("physical");
			LinearFuncEntry atk = helper.regLinear("physical_attack", 0.2, 0.1);
			LinearFuncEntry armor = helper.regLinear("physical_armor", 4, 2);
			LinearFuncEntry factor = helper.regLinear("physical_reduce_magic", 0.5, 0);

			EFF_PHYSICAL_DAMAGE = helper.setEffect("physical_damage", () -> new PhysicalDamageEffect(
					new AttrSetEntry(Attributes.ATTACK_DAMAGE,
							AttributeModifier.Operation.ADD_MULTIPLIED_BASE, atk, true),
					factor)).desc("Barbaric Attack",
					"Magical damage dealt will be reduced to %s%%"
			).register();
			EFF_PHYSICAL_ARMOR = helper.setEffect("physical_armor", () -> new AttributeSetEffect(
					new AttrSetEntry(Attributes.ARMOR,
							ADD_VALUE, armor, false)
			)).lang("Survival Instinct").register();

			SET_PHYSICAL = helper.regSet(1, 5, "Courage Set")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_PHYSICAL_DAMAGE.get())
							.add(5, EFF_PHYSICAL_ARMOR.get()))
					.register();
		}

		// wrath
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("wrath");
			LinearFuncEntry dec = helper.regLinear("wrath_decrease", 0.8, 0);
			LinearFuncEntry inc = helper.regLinear("wrath_increase", 1.2, 0.1);
			EFF_WRATH_POISON = helper.setEffect("wrath_poison", () -> new WrathEffect(e -> e.hasEffect(MobEffects.POISON), dec, inc))
					.desc("Bad Day Encounters Bad Luck",
							"When target is poisoned, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();
			EFF_WRATH_SLOW = helper.setEffect("wrath_slow", () -> new WrathEffect(e -> e.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), dec, inc))
					.desc("Snow Storm Encounters Blitz Winter",
							"When target is slowed, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();
			EFF_WRATH_FIRE = helper.setEffect("wrath_fire", () -> new WrathEffect(Entity::isOnFire, dec, inc))
					.desc("Emergency Encounters Unwanted Fight",
							"When target is on fire, increase damage to %s%%. Otherwise, decrease damage to %s%%."
					).register();

			SET_WRATH = helper.regSet(1, 5, "Curse of Bad Luck")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, EFF_WRATH_POISON.get())
							.add(3, EFF_WRATH_SLOW.get())
							.add(5, EFF_WRATH_FIRE.get()))
					.register();
		}

	}

	public static void register() {

	}

}
