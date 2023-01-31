package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.TimedCASetEffect;
import dev.xkmc.l2artifacts.content.effects.persistent.SimpleCPSetEffect;
import dev.xkmc.l2artifacts.content.effects.v4.*;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class LAItem4 {

	//v4
	public static final SetEntry<ArtifactSet> SET_ANCIENT;
	public static final SetEntry<ArtifactSet> SET_LUCKLOVER;
	public static final SetEntry<ArtifactSet> SET_ABYSSMEDAL;
	public static final SetEntry<ArtifactSet> SET_LONGSHOOTER;
	public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_1;
	public static final RegistryEntry<SimpleCPSetEffect> EFF_ANCIENT_2;
	public static final RegistryEntry<AttackStrikeEffect> EFF_ANCIENT_3;
	public static final RegistryEntry<ImmobileEffect> EFF_ANCIENT_4;
	public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_5;
	public static final RegistryEntry<LongShooterEffect> EFF_LONGSHOOTER_3;
	public static final RegistryEntry<LongShooterPersistentEffect> EFF_LONGSHOOTER_4;
	public static final RegistryEntry<LuckAttackEffect> EFF_LUCKCLOVER_3;
	public static final RegistryEntry<LuckAttackEffect> EFF_LUCKCLOVER_4;
	public static final RegistryEntry<AttributeSetEffect> EFF_ABYSSMEDAL_3;
	public static final RegistryEntry<AbyssAttackEffect> EFF_ABYSSMEDAL_5;

	static {
		//ancient
		{
			LinearFuncEntry threshold = REGISTRATE.regLinear("ancient_threshold", 20, 0);
			LinearFuncEntry speed = REGISTRATE.regLinear("ancient_speed", 0.2, 0.1);
			LinearFuncEntry period = REGISTRATE.regLinear("ancient_heal_period", 60, 0);
			LinearFuncEntry heal = REGISTRATE.regLinear("ancient_heal", 2, 1);
			LinearFuncEntry duration = REGISTRATE.regLinear("ancient_strike_duration", 40, 20);
			LinearFuncEntry count = REGISTRATE.regLinear("ancient_strike_count", 3, 0);
			LinearFuncEntry attack = REGISTRATE.regLinear("ancient_attack", 0.2, 0.1);
			LinearFuncEntry protection = REGISTRATE.regLinear("ancient_protection", 0.8, -0.1);
			LinearFuncEntry speed5 = REGISTRATE.regLinear("ancient_speed_5", 0.2, 0.1);
			LinearFuncEntry attack5 = REGISTRATE.regLinear("ancient_attack_5", 0.2, 0.1);

			EFF_ANCIENT_1 = REGISTRATE.setEffect("ancient_scroll_1", () -> new TimedCASetEffect(Entity::isSprinting, threshold,
							new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, speed, true)))
					.desc("Run lick wind",
							"After sprinting for %s seconds:"
					).register();
			EFF_ANCIENT_2 = REGISTRATE.setEffect("ancient_scroll_2", () -> new SimpleCPSetEffect(period,
					e -> !e.isSprinting(),
					(e, rank) -> e.heal((float) heal.getFromRank(rank)),
					(rank, id) -> Component.translatable(id, period.getFromRank(rank) / 20d, heal.getFromRank(rank))
			)).desc("Recover like plant",
					"Every %s seconds, heal %s health point"
			).register();
			EFF_ANCIENT_3 = REGISTRATE.setEffect("ancient_scroll_3", () -> new AttackStrikeEffect(duration, count,
							new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, attack, true)))
					.desc("Plunder like fire",
							"After attacking with full power for %s strikes with interval less than %s seconds:"
					).register();
			EFF_ANCIENT_4 = REGISTRATE.setEffect("ancient_scroll_4", () -> new ImmobileEffect(protection, threshold))
					.desc("Immovable as mountain",
							"After stay still for %s seconds: Damage taken is reduced to %s%% of original"
					).register();
			EFF_ANCIENT_5 = REGISTRATE.setEffect("ancient_scroll_5", () -> new TimedCASetEffect(Entity::isShiftKeyDown, threshold,
							new AttrSetEntry(() -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, speed5, true),
							new AttrSetEntry(() -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, attack5, true)
					))
					.desc("As dark as dark clouds",
							"After sneaking for %s seconds:"
					).register();

			SET_ANCIENT = Wrappers.cast(REGISTRATE.regSet("ancient_scroll", ArtifactSet::new, 1, 5, "Ancient Scroll")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(1, LAItem4.EFF_ANCIENT_1.get())
							.add(2, LAItem4.EFF_ANCIENT_2.get())
							.add(3, LAItem4.EFF_ANCIENT_3.get())
							.add(4, LAItem4.EFF_ANCIENT_4.get())
							.add(5, LAItem4.EFF_ANCIENT_5.get()))
					.register());

		}

		{//Lucky clover
			LinearFuncEntry luck_threshold = REGISTRATE.regLinear("luck_threshold", 40, 0);
			LinearFuncEntry luck_count_3 = REGISTRATE.regLinear("luck_count_3", 3, 0);
			LinearFuncEntry luck_count_4 = REGISTRATE.regLinear("luck_count_4", 4, 0);
			LinearFuncEntry luck_rate = REGISTRATE.regLinear("luck_rate", 1, 0);
			LinearFuncEntry luck_dmg = REGISTRATE.regLinear("luck_dmg", 1, 0.2);

			EFF_LUCKCLOVER_3 = REGISTRATE.setEffect("luck_clover_3", () -> new LuckAttackEffect(luck_threshold, luck_count_3,
							new AttrSetEntry(CRIT_DMG, ADDITION, luck_dmg, true)))
					.desc("Lucky number : 3",
							"The %s consecutive attacks are all within %s second:"
					).register();
			EFF_LUCKCLOVER_4 = REGISTRATE.setEffect("luck_clover_4", () -> new LuckAttackEffect(luck_threshold, luck_count_4,
							new AttrSetEntry(CRIT_RATE, ADDITION, luck_rate, true)))
					.desc("Lucky number : 4",
							"Must be critical hit! The %s consecutive attacks are all within %s second:"
					).register();

			SET_LUCKLOVER = Wrappers.cast(REGISTRATE.regSet("luck_clover", ArtifactSet::new, 4, 4, "Luck Clover")
					.setSlots(SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem4.EFF_LUCKCLOVER_3.get())
							.add(4, LAItem4.EFF_LUCKCLOVER_4.get())
					)
					.register());

		}
		{//abyss eclipse medal
			LinearFuncEntry abyss_level = REGISTRATE.regLinear("abyss_level", 0, 0.2);
			LinearFuncEntry abyss_health = REGISTRATE.regLinear("abyss_health", 0.4, 0.2);
			LinearFuncEntry abyss_duration = REGISTRATE.regLinear("abyss_duration", 80, 20);
			LinearFuncEntry abyss_hurt = REGISTRATE.regLinear("abyss_hurt", 1.2, 0.2);

			EFF_ABYSSMEDAL_3 = REGISTRATE.setEffect("abyss_medal_3", () -> new AttributeSetEffect(
							new AttrSetEntry(() -> Attributes.MAX_HEALTH, MULTIPLY_BASE, abyss_health, true)))
					.desc("Abyss strengthens your body",
							"The abyss will give you the power of blood and flesh."
					).register();
			EFF_ABYSSMEDAL_5 = REGISTRATE.setEffect("abyss_medal_5", () -> new AbyssAttackEffect(abyss_duration, abyss_level, abyss_hurt, 0))
					.desc("Abyss eclipse",
							"The power of the abyss is attached to your weapon and will bring %s second Lv%s Weakness and Wither to the enemy, but you will also receive %s%% damage."
					).register();

			SET_ABYSSMEDAL = Wrappers.cast(REGISTRATE.regSet("abyss_medal", ArtifactSet::new, 1, 5, "Abyss Medal")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem4.EFF_ABYSSMEDAL_3.get())
							.add(5, LAItem4.EFF_ABYSSMEDAL_5.get())
					)
					.register());


		}
		{//Long range shooter
			LinearFuncEntry long_shooter_atk = REGISTRATE.regLinear("long_shooter_atk", 0.8, 0.4);
			EFF_LONGSHOOTER_3 = REGISTRATE.setEffect("long_shooter_3", () -> new LongShooterEffect(new AttrSetEntry(BOW_STRENGTH, MULTIPLY_BASE, long_shooter_atk, true)))
					.desc("Focus of the long-range shooter",
							"When there is no Monster in the nearby 8 cells:")
					.register();

			EFF_LONGSHOOTER_4 = REGISTRATE.setEffect("long_shooter_4", () -> new LongShooterPersistentEffect(new AttrSetEntry(BOW_STRENGTH, MULTIPLY_BASE, long_shooter_atk, true)))
					.desc("Last chance",
							"Set the effect of suit 3 to 6 squares, when approached, it still lasts for two seconds and gains two second acceleration"
					).register();


			SET_LONGSHOOTER = Wrappers.cast(REGISTRATE.regSet("long_shooter", ArtifactSet::new, 1, 5, "Long Shooter")
					.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BRACELET, SLOT_BELT).regItems()
					.buildConfig((c) -> c
							.add(3, LAItem4.EFF_LONGSHOOTER_3.get())
							.add(4, LAItem4.EFF_LONGSHOOTER_4.get())
					)
					.register());

		}

	}

	public static void register() {

	}

}
