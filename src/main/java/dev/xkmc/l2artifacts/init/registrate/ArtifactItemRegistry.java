package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.SimpleCASetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.TimedCASetEffect;
import dev.xkmc.l2artifacts.content.effects.persistent.SimpleCPSetEffect;
import dev.xkmc.l2artifacts.content.effects.v1.*;
import dev.xkmc.l2artifacts.content.effects.v2.*;
import dev.xkmc.l2artifacts.content.effects.v3.*;
import dev.xkmc.l2artifacts.content.effects.v4.*;
import dev.xkmc.l2artifacts.content.misc.ExpItem;
import dev.xkmc.l2artifacts.content.misc.RandomArtifactItem;
import dev.xkmc.l2artifacts.content.misc.SelectArtifactItem;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.generators.ModelFile;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry.*;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

@SuppressWarnings({"raw_type", "unchecked"})
public class ArtifactItemRegistry {

	public static final Tab TAB_ARTIFACT = new Tab("artifacts");

	static {
		REGISTRATE.creativeModeTab(() -> TAB_ARTIFACT);
	}

	public static final ItemEntry<SelectArtifactItem> SELECT;
	public static final ItemEntry<RandomArtifactItem>[] RANDOM;
	public static final ItemEntry<ExpItem>[] ITEM_EXP;
	public static final ItemEntry<StatContainerItem>[] ITEM_STAT;
	public static final ItemEntry<UpgradeBoostItem>[] ITEM_BOOST_MAIN, ITEM_BOOST_SUB;

	static {
		SELECT = REGISTRATE.item("select", SelectArtifactItem::new)
				.defaultModel().lang("Artifact Selector (Creative)").register();
		int n = 5;
		RANDOM = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			RANDOM[i] = REGISTRATE.item("random_" + r, p -> new RandomArtifactItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/rank/" + r))
							.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/random")))
					.lang("Random Artifact Lv." + r).register();
		}
		ITEM_EXP = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_EXP[i] = REGISTRATE.item("artifact_experience_" + r, p -> new ExpItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/rank/" + r))
							.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/artifact_experience")))
					.lang("Artifact Experience Lv." + r).register();
		}
		ITEM_STAT = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_STAT[i] = REGISTRATE.item("stat_container_" + r, p -> new StatContainerItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/rank/" + r))
							.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/stat_container")))
					.lang("Stat Container Lv." + r).register();
		}
		ITEM_BOOST_MAIN = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_BOOST_MAIN[i] = REGISTRATE.item("boost_main_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_MAIN_STAT))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/rank/" + r))
							.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/boost_main")))
					.lang("Main Stat Booster Lv." + r).register();
		}
		ITEM_BOOST_SUB = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_BOOST_SUB[i] = REGISTRATE.item("boost_sub_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_SUB_STAT))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", new ResourceLocation(L2Artifacts.MODID, "item/rank/" + r))
							.texture("layer1", new ResourceLocation(L2Artifacts.MODID, "item/boost_sub")))
					.lang("Sub Stat Booster Lv." + r).register();
		}
	}

	// bland
	public static final SetEntry<ArtifactSet> SET_GAMBLER, SET_BERSERKER, SET_ARCHER;

	public static final RegistryEntry<AttributeSetEffect> EFF_GAMBLER_3, EFF_GAMBLER_5, EFF_BERSERKER_3, EFF_BERSERKER_5, EFF_ARCHER_3, EFF_ARCHER_5;

	// v1
	public static final SetEntry<ArtifactSet> SET_SAINT, SET_PERFECTION, SET_DAMOCLES, SET_PROTECTION;

	public static final RegistryEntry<PerfectionAbsorption> EFF_PERFECTION_ABSORPTION;
	public static final RegistryEntry<PerfectionProtection> EFF_PERFECTION_PROTECTION;
	public static final RegistryEntry<SaintReduction> EFF_SAINT_REDUCTION;
	public static final RegistryEntry<SaintRestoration> EFF_SAINT_RESTORATION;
	public static final RegistryEntry<DamoclesSword> EFF_DAMOCLES;
	public static final RegistryEntry<ProtectionResistance> EFF_PROTECTION_RESISTANCE;

	// v2
	public static final SetEntry<ArtifactSet> SET_FROZE, SET_EXECUTOR, SET_PHYSICAL, SET_WRATH;

	public static final RegistryEntry<FrozeSlowEffect> EFF_FROZE_SLOW;
	public static final RegistryEntry<FrozeBreakEffect> EFF_FROZE_BREAK;
	public static final RegistryEntry<ExecutorSelfHurtEffect> EFF_EXECUTOR_SELF_HURT;
	public static final RegistryEntry<ExecutorLimitEffect> EFF_EXECUTOR_LIMIT;
	public static final RegistryEntry<PhysicalDamageEffect> EFF_PHYSICAL_DAMAGE;
	public static final RegistryEntry<AttributeSetEffect> EFF_PHYSICAL_ARMOR;
	public static final RegistryEntry<WrathEffect> EFF_WRATH_POISON, EFF_WRATH_SLOW, EFF_WRATH_FIRE;

	// v3
	public static final SetEntry<ArtifactSet> SET_PHOTOSYN, SET_VAMPIRE, SET_SUN_BLOCK, SET_GLUTTONY, SET_FALLEN;

	public static final RegistryEntry<Photosynthesisffect> EFF_PHOTOSYN;
	public static final RegistryEntry<VampireBurn> EFF_VAMPIRE_BURN;
	public static final RegistryEntry<VampireHeal> EFF_VAMPIRE_HEAL;
	public static final RegistryEntry<SunBlockMask> EFF_SUN_BLOCK;
	public static final RegistryEntry<SimpleCASetEffect> EFF_GLUTTONY_FAST;
	public static final RegistryEntry<GluttonyHeal> EFF_GLUTTONY_HEAL;
	public static final RegistryEntry<SimpleCASetEffect> EFF_FALLEN_1, EFF_FALLEN_2, EFF_FALLEN_3, EFF_FALLEN_4, EFF_FALLEN_5;

	//v4
	public static final SetEntry<ArtifactSet> SET_ANCIENT, SET_LUCKLOVER, SET_ABYSSMEDAL, SET_LONGSHOOTER;

	public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_1;
	public static final RegistryEntry<SimpleCPSetEffect> EFF_ANCIENT_2;
	public static final RegistryEntry<AttackStrikeEffect> EFF_ANCIENT_3;
	public static final RegistryEntry<ImmobileEffect> EFF_ANCIENT_4;
	public static final RegistryEntry<TimedCASetEffect> EFF_ANCIENT_5;

	public static final RegistryEntry<LongShooterEffect> EFF_LONGSHOOTER_3;
	public static final RegistryEntry<LongShooterPersistentEffect> EFF_LONGSHOOTER_4;
	public static final RegistryEntry<LuckAttackEffect> EFF_LUCKCLOVER_3, EFF_LUCKCLOVER_4;

	public static final RegistryEntry<AttributeSetEffect> EFF_ABYSSMEDAL_3;
	public static final RegistryEntry<AbyssAttackEffect> EFF_ABYSSMEDAL_5;

	static {

		// v0
		{
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
								.add(3, EFF_GAMBLER_3.get())
								.add(5, EFF_GAMBLER_5.get()))
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
								.add(3, EFF_BERSERKER_3.get())
								.add(5, EFF_BERSERKER_5.get()))
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
								.add(3, EFF_ARCHER_3.get())
								.add(5, EFF_ARCHER_5.get()))
						.register());
			}
		}

		// v1
		{
			// saint
			{
				LinearFuncEntry atk = REGISTRATE.regLinear("saint_reduction_atk", 0.25, 0);
				LinearFuncEntry def = REGISTRATE.regLinear("saint_reduction_def", 0.25, 0.05);
				LinearFuncEntry period = REGISTRATE.regLinear("saint_restoration", 100, -10);

				EFF_SAINT_REDUCTION = REGISTRATE.setEffect("saint_reduction", () -> new SaintReduction(atk, def))
						.desc("Sympathy of Saint",
								"Direct damage dealt reduce to %s%%, damage taken reduce to %s%%"
						).register();
				EFF_SAINT_RESTORATION = REGISTRATE.setEffect("saint_restoration", () -> new SaintRestoration(period))
						.desc("Bless of Holiness",
								"When have empty main hand, restore health to oneself or allies every %s seconds."
						).register();

				SET_SAINT = Wrappers.cast(REGISTRATE.regSet("saint", ArtifactSet::new, 1, 5, "Saint Set")
						.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(3, EFF_SAINT_REDUCTION.get())
								.add(5, EFF_SAINT_RESTORATION.get()))
						.register());
			}

			// perfection
			{
				LinearFuncEntry period = REGISTRATE.regLinear("perfection_absorption_period", 100, 0);
				LinearFuncEntry max = REGISTRATE.regLinear("perfection_absorption_max", 4, 2);
				LinearFuncEntry def = REGISTRATE.regLinear("perfection_protection", 0.2, 0.1);
				EFF_PERFECTION_ABSORPTION = REGISTRATE.setEffect("perfection_absorption", () -> new PerfectionAbsorption(period, max))
						.desc("Heart of Perfection",
								"When at full health, every %s seconds gain 1 point of absorption, maximum %s points."
						).register();
				EFF_PERFECTION_PROTECTION = REGISTRATE.setEffect("perfection_protection", () -> new PerfectionProtection(def))
						.desc("Eternity of Perfection",
								"When at full health, reduce damage by %s%%"
						).register();

				SET_PERFECTION = Wrappers.cast(REGISTRATE.regSet("perfection", ArtifactSet::new, 1, 5, "Perfection Set")
						.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(2, EFF_PERFECTION_PROTECTION.get())
								.add(4, EFF_PERFECTION_ABSORPTION.get()))
						.register());
			}

			//damocles
			{

				LinearFuncEntry amplify = REGISTRATE.regLinear("damocles", 1, 0.5);
				EFF_DAMOCLES = REGISTRATE.setEffect("damocles", () -> new DamoclesSword(amplify))
						.desc("Sword of Damocles",
								"When at full health, direct attack damage increase by %s%%. When below half health, die immediately."
						).register();

				SET_DAMOCLES = Wrappers.cast(REGISTRATE.regSet("damocles", ArtifactSet::new, 1, 5, "Sword of Damocles")
						.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
								.add(1, EFF_DAMOCLES.get()))
						.register());
			}

			// protection
			{
				EFF_PROTECTION_RESISTANCE = REGISTRATE.setEffect("protection_resistance", ProtectionResistance::new)
						.desc("Crown of Never Falling Soldier",
								"Damage taken reduced when health is low."
						).register();

				SET_PROTECTION = Wrappers.cast(REGISTRATE.regSet("protection", ArtifactSet::new, 1, 5, "Never Falling Crown")
						.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
								.add(1, EFF_PROTECTION_RESISTANCE.get()))
						.register());
			}
		}

		// v2
		{
			//froze
			{
				LinearFuncEntry damage = REGISTRATE.regLinear("froze_slow_fire_damage", 1.2, 0);
				LinearFuncEntry period = REGISTRATE.regLinear("froze_slow_period", 80, 40);
				LinearFuncEntry level = REGISTRATE.regLinear("froze_slow_level", 0, 1);
				LinearFuncEntry factor = REGISTRATE.regLinear("froze_break", 0.2, 0.1);
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
								.add(3, EFF_FROZE_SLOW.get())
								.add(5, EFF_FROZE_BREAK.get()))
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
								.add(3, EFF_EXECUTOR_SELF_HURT.get())
								.add(5, EFF_EXECUTOR_LIMIT.get()))
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
								.add(3, EFF_PHYSICAL_DAMAGE.get())
								.add(5, EFF_PHYSICAL_ARMOR.get()))
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
								.add(1, EFF_WRATH_POISON.get())
								.add(3, EFF_WRATH_SLOW.get())
								.add(5, EFF_WRATH_FIRE.get()))
						.register());
			}
		}

		// v3
		{
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
								.add(1, EFF_PHOTOSYN.get()))
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
								.add(1, EFF_VAMPIRE_BURN.get())
								.add(4, EFF_VAMPIRE_HEAL.get()))
						.register());
			}

			// sun block
			{
				EFF_SUN_BLOCK = REGISTRATE.setEffect("sun_block", SunBlockMask::new)
						.desc("Sunlight Hat", "Block sunlight for the player").register();

				SET_SUN_BLOCK = Wrappers.cast(REGISTRATE.regSet("sun_block", ArtifactSet::new, 1, 5, "Umbrella")
						.setSlots(SLOT_HEAD).regItems().buildConfig((c) -> c
								.add(1, EFF_SUN_BLOCK.get()))
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
								.add(3, EFF_GLUTTONY_FAST.get())
								.add(5, EFF_GLUTTONY_HEAL.get()))
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
								.add(1, EFF_FALLEN_1.get())
								.add(2, EFF_FALLEN_2.get())
								.add(3, EFF_FALLEN_3.get())
								.add(4, EFF_FALLEN_4.get())
								.add(5, EFF_FALLEN_5.get()))
						.register());
			}


		}

		//v4
		{
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

				SET_ANCIENT = Wrappers.cast(REGISTRATE.regSet("ancient_scroll", ArtifactSet::new, 1, 5, "AncientScroll Set")
						.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(1, EFF_ANCIENT_1.get())
								.add(2, EFF_ANCIENT_2.get())
								.add(3, EFF_ANCIENT_3.get())
								.add(4, EFF_ANCIENT_4.get())
								.add(5, EFF_ANCIENT_5.get()))
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

				SET_LUCKLOVER = Wrappers.cast(REGISTRATE.regSet("luck_clover", ArtifactSet::new, 4, 4, "LuckClover Set")
						.setSlots(SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(3, EFF_LUCKCLOVER_3.get())
								.add(4, EFF_LUCKCLOVER_4.get())
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

				SET_ABYSSMEDAL = Wrappers.cast(REGISTRATE.regSet("abyss_medal", ArtifactSet::new, 1, 5, "AbyssMedal Set")
						.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BODY, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(3, EFF_ABYSSMEDAL_3.get())
								.add(5, EFF_ABYSSMEDAL_5.get())
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


				SET_LONGSHOOTER = Wrappers.cast(REGISTRATE.regSet("long_shooter", ArtifactSet::new, 1, 5, "LongShooter Set")
						.setSlots(SLOT_HEAD, SLOT_NECKLACE, SLOT_BRACELET, SLOT_BELT).regItems()
						.buildConfig((c) -> c
								.add(3, EFF_LONGSHOOTER_3.get())
								.add(4, EFF_LONGSHOOTER_4.get())
						)
						.register());

			}


		}


	}

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