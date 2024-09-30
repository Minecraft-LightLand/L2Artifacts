package dev.xkmc.l2artifacts.init.registrate.items;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.TimedCASetEffect;
import dev.xkmc.l2artifacts.content.effects.persistent.SimpleCPSetEffect;
import dev.xkmc.l2artifacts.content.effects.v4.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEffectEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static dev.xkmc.l2damagetracker.init.L2DamageTracker.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class LAItem4 {

	//v4
	public static final SetEntry<ArtifactSet> SET_ANCIENT;
	public static final SetEntry<ArtifactSet> SET_LUCKCLOVER;
	public static final SetEntry<ArtifactSet> SET_ABYSSMEDAL;
	public static final SetEntry<ArtifactSet> SET_LONGSHOOTER;
	public static final SetEffectEntry<TimedCASetEffect> EFF_ANCIENT_1;
	public static final SetEffectEntry<SimpleCPSetEffect> EFF_ANCIENT_2;
	public static final SetEffectEntry<AttackStrikeEffect> EFF_ANCIENT_3;
	public static final SetEffectEntry<ImmobileEffect> EFF_ANCIENT_4;
	public static final SetEffectEntry<TimedCASetEffect> EFF_ANCIENT_5;
	public static final SetEffectEntry<LongShooterEffect> EFF_LONGSHOOTER_3;
	public static final SetEffectEntry<LongShooterPersistentEffect> EFF_LONGSHOOTER_4;
	public static final SetEffectEntry<LuckAttackEffect> EFF_LUCKCLOVER_3;
	public static final SetEffectEntry<LuckAttackEffect> EFF_LUCKCLOVER_4;
	public static final SetEffectEntry<AttributeSetEffect> EFF_ABYSSMEDAL_3;
	public static final SetEffectEntry<AbyssAttackEffect> EFF_ABYSSMEDAL_5;

	static {
		//ancient
		{
			SetRegHelper helper = REGISTRATE.getSetHelper("ancient_scroll");
			LinearFuncEntry threshold = helper.regLinear("ancient_threshold", 20, 0);
			LinearFuncEntry speed = helper.regLinear("ancient_speed", 0.2, 0.1);
			LinearFuncEntry period = helper.regLinear("ancient_heal_period", 60, 0);
			LinearFuncEntry heal = helper.regLinear("ancient_heal", 2, 1);
			LinearFuncEntry duration = helper.regLinear("ancient_strike_duration", 40, 20);
			LinearFuncEntry count = helper.regLinear("ancient_strike_count", 3, 0);
			LinearFuncEntry attack = helper.regLinear("ancient_attack", 0.2, 0.1);
			LinearFuncEntry protection = helper.regLinear("ancient_protection", 0.8, -0.1);
			LinearFuncEntry speed5 = helper.regLinear("ancient_speed_5", 0.2, 0.1);
			LinearFuncEntry attack5 = helper.regLinear("ancient_attack_5", 0.2, 0.1);

			EFF_ANCIENT_1 = helper.setEffect("ancient_scroll_1", () -> new TimedCASetEffect(Entity::isSprinting, threshold,
							new AttrSetEntry(Attributes.MOVEMENT_SPEED, ADD_MULTIPLIED_BASE, speed, true)))
					.desc("Run like wind",
							"After sprinting for %s seconds:"
					).register();
			EFF_ANCIENT_2 = helper.setEffect("ancient_scroll_2", () -> new SimpleCPSetEffect(period,
					e -> !e.isSprinting(),
					(e, rank) -> e.heal((float) heal.getFromRank(rank)),
					(rank, id) -> Component.translatable(id, period.getFromRank(rank) / 20d, heal.getFromRank(rank))
			)).desc("Recover like plant",
					"Every %s seconds, heal %s health point"
			).register();
			EFF_ANCIENT_3 = helper.setEffect("ancient_scroll_3", () -> new AttackStrikeEffect(duration, count,
							new AttrSetEntry(Attributes.ATTACK_DAMAGE, ADD_MULTIPLIED_BASE, attack, true)))
					.desc("Plunder like fire",
							"After attacking with full power for %s strikes with interval less than %s seconds:"
					).register();
			EFF_ANCIENT_4 = helper.setEffect("ancient_scroll_4", () -> new ImmobileEffect(protection, threshold))
					.desc("Solid as mountain",
							"After stay still for %s seconds: Damage taken is reduced to %s%% of original"
					).register();
			EFF_ANCIENT_5 = helper.setEffect("ancient_scroll_5", () -> new TimedCASetEffect(Entity::isShiftKeyDown, threshold,
							new AttrSetEntry(Attributes.MOVEMENT_SPEED, ADD_MULTIPLIED_BASE, speed5, true),
							new AttrSetEntry(Attributes.ATTACK_DAMAGE, ADD_MULTIPLIED_BASE, attack5, true)
					))
					.desc("Dark as night sky",
							"After sneaking for %s seconds:"
					).register();

			SET_ANCIENT = helper.regSet(1, 5, "Ancient Scroll")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, EFF_ANCIENT_1.get())
							.add(2, EFF_ANCIENT_2.get())
							.add(3, EFF_ANCIENT_3.get())
							.add(4, EFF_ANCIENT_4.get())
							.add(5, EFF_ANCIENT_5.get()))
					.register();

		}

		{//Lucky clover
			SetRegHelper helper = REGISTRATE.getSetHelper("luck_clover");
			LinearFuncEntry luck_threshold = helper.regLinear("luck_threshold", 40, 0);
			LinearFuncEntry luck_count_3 = helper.regLinear("luck_count_3", 3, 0);
			LinearFuncEntry luck_count_4 = helper.regLinear("luck_count_4", 4, 0);
			LinearFuncEntry luck_rate = helper.regLinear("luck_rate", 1, 0);
			LinearFuncEntry luck_dmg = helper.regLinear("luck_dmg", 1, 0.2);

			EFF_LUCKCLOVER_3 = helper.setEffect("luck_clover_3", () -> new LuckAttackEffect(luck_threshold, luck_count_3,
							new AttrSetEntry(CRIT_DMG, ADD_VALUE, luck_dmg, true)))
					.desc("Lucky number : 3",
							"The %s consecutive attacks are all within %s second:"
					).register();
			EFF_LUCKCLOVER_4 = helper.setEffect("luck_clover_4", () -> new LuckAttackEffect(luck_threshold, luck_count_4,
							new AttrSetEntry(CRIT_RATE, ADD_VALUE, luck_rate, true)))
					.desc("Lucky number : 4",
							"The %s consecutive attacks are all within %s second:"
					).register();

			SET_LUCKCLOVER = helper.regSet(4, 4, "Luck Clover")
					.setSlots(SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_LUCKCLOVER_3.get())
							.add(4, EFF_LUCKCLOVER_4.get())
					)
					.register();

		}
		{//abyss eclipse medal
			SetRegHelper helper = REGISTRATE.getSetHelper("abyss_medal");
			LinearFuncEntry abyss_level = helper.regLinear("abyss_level", 0, 0.2);
			LinearFuncEntry abyss_health = helper.regLinear("abyss_health", 0.4, 0.2);
			LinearFuncEntry abyss_duration = helper.regLinear("abyss_duration", 80, 20);
			LinearFuncEntry abyss_hurt = helper.regLinear("abyss_hurt", 0.2, 0.1);

			EFF_ABYSSMEDAL_3 = helper.setEffect("abyss_medal_3", () -> new AttributeSetEffect(
							new AttrSetEntry(Attributes.MAX_HEALTH, ADD_MULTIPLIED_BASE, abyss_health, true)))
					.lang("Abyss Aggregate").register();
			EFF_ABYSSMEDAL_5 = helper.setEffect("abyss_medal_5", () -> new AbyssAttackEffect(abyss_duration, abyss_level, abyss_hurt, 0))
					.desc("Abyss Eclipse",
							"On attack, inflict %s and %s. -%s%% magic damage you take."
					).register();

			SET_ABYSSMEDAL = helper.regSet(1, 5, "Abyss Medal")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_ABYSSMEDAL_3.get())
							.add(5, EFF_ABYSSMEDAL_5.get())
					)
					.register();


		}
		{//Long range shooter
			SetRegHelper helper = REGISTRATE.getSetHelper("long_shooter");
			LinearFuncEntry long_shooter_atk = helper.regLinear("long_shooter_atk", 0.6, 0.3);
			EFF_LONGSHOOTER_3 = helper.setEffect("long_shooter_3", () -> new LongShooterEffect(
							new AttrSetEntry(BOW_STRENGTH, ADD_VALUE, long_shooter_atk, true)))
					.desc("Focus of the long-range shooter",
							"When there is no Monster in the nearby 8 blocks:")
					.register();

			EFF_LONGSHOOTER_4 = helper.setEffect("long_shooter_4", LongShooterPersistentEffect::new)
					.desc("Last chance",
							"Focus distance above becomes 6 blocks. When approached, it still lasts for two seconds and gains two second acceleration"
					).register();


			SET_LONGSHOOTER = helper.regSet(1, 5, "Long Shooter")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, EFF_LONGSHOOTER_3.get())
							.add(4, EFF_LONGSHOOTER_4.get())
					)
					.register();

		}

	}

	public static void register() {

	}

}
