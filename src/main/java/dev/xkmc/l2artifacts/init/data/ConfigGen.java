package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.config.LinearFuncConfig;
import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class ConfigGen extends ConfigDataProvider {

	public static final ResourceLocation HEALTH_ADD = regLang("health_add");
	public static final ResourceLocation ARMOR_ADD = regLang("armor_add");
	public static final ResourceLocation TOUGH_ADD = regLang("tough_add");
	public static final ResourceLocation ATK_ADD = regLang("attack_add");
	public static final ResourceLocation REACH_ADD = regLang("reach_add");
	public static final ResourceLocation CR_ADD = regLang("crit_rate_add");
	public static final ResourceLocation CD_ADD = regLang("crit_damage_add");
	public static final ResourceLocation ATK_MULT = regLang("attack_mult");
	public static final ResourceLocation SPEED_MULT = regLang("speed_mult");
	public static final ResourceLocation ATK_SPEED_MULT = regLang("attack_speed_mult");
	public static final ResourceLocation BOW_ADD = regLang("bow_strength_add");

	private static ResourceLocation regLang(String name) {
		L2Artifacts.REGISTRATE.addRawLang("stat_type." + L2Artifacts.MODID + "." + name, RegistrateLangProvider.toEnglishName(name));
		return new ResourceLocation(L2Artifacts.MODID, name);
	}

	public static void register() {

	}

	public ConfigGen(DataGenerator generator) {
		super(generator, "Artifact Config");
	}

	@Override
	public void add(Collector map) {

		// Stat Type Config
		regStat(map, HEALTH_ADD, Attributes.MAX_HEALTH, ADDITION, false, 0.4);
		regStat(map, ARMOR_ADD, Attributes.ARMOR, ADDITION, false, 0.4);
		regStat(map, TOUGH_ADD, Attributes.ARMOR_TOUGHNESS, ADDITION, false, 0.2);
		regStat(map, ATK_ADD, Attributes.ATTACK_DAMAGE, ADDITION, false, 0.4);
		regStat(map, REACH_ADD, ForgeMod.ENTITY_REACH.get(), ADDITION, false, 0.02);
		regStat(map, CR_ADD, L2DamageTracker.CRIT_RATE.get(), ADDITION, true, 0.01);
		regStat(map, CD_ADD, L2DamageTracker.CRIT_DMG.get(), ADDITION, true, 0.02);
		regStat(map, ATK_MULT, Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, true, 0.02);
		regStat(map, SPEED_MULT, Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, true, 0.01);
		regStat(map, ATK_SPEED_MULT, Attributes.ATTACK_SPEED, MULTIPLY_BASE, true, 0.01);
		regStat(map, BOW_ADD, L2DamageTracker.BOW_STRENGTH.get(), ADDITION, true, 0.02);
		// Slot Stat Config
		{

			ArrayList<ResourceLocation> all = new ArrayList<>();
			{
				all.add(HEALTH_ADD);
				all.add(ARMOR_ADD);
				all.add(TOUGH_ADD);
				all.add(ATK_ADD);
				all.add(ATK_MULT);
				all.add(CR_ADD);
				all.add(CD_ADD);
				all.add(REACH_ADD);
				all.add(ATK_SPEED_MULT);
				all.add(SPEED_MULT);
				all.add(BOW_ADD);
			}
			{
				ArrayList<ResourceLocation> list = new ArrayList<>();
				list.add(HEALTH_ADD);
				list.add(ARMOR_ADD);
				list.add(SPEED_MULT);
				list.add(CR_ADD);
				list.add(TOUGH_ADD);
				addSlotStat(map, ArtifactTypeRegistry.SLOT_BODY.get(), list, all);
			}
			{
				ArrayList<ResourceLocation> list = new ArrayList<>();
				list.add(ATK_ADD);
				list.add(ATK_MULT);
				list.add(BOW_ADD);
				list.add(CD_ADD);
				list.add(REACH_ADD);
				addSlotStat(map, ArtifactTypeRegistry.SLOT_BRACELET.get(), list, all);
			}
			{
				ArrayList<ResourceLocation> list = new ArrayList<>();
				list.add(HEALTH_ADD);
				list.add(ARMOR_ADD);
				list.add(ATK_ADD);
				list.add(ATK_MULT);
				list.add(BOW_ADD);

				list.add(CR_ADD);
				list.add(CD_ADD);
				addSlotStat(map, ArtifactTypeRegistry.SLOT_NECKLACE.get(), list, all);
			}
			{
				ArrayList<ResourceLocation> list = new ArrayList<>();
				list.add(HEALTH_ADD);
				list.add(ARMOR_ADD);
				list.add(ATK_ADD);
				list.add(ATK_MULT);
				list.add(BOW_ADD);

				list.add(ATK_SPEED_MULT);
				list.add(SPEED_MULT);
				addSlotStat(map, ArtifactTypeRegistry.SLOT_BELT.get(), list, all);
			}
			{
				ArrayList<ResourceLocation> list = new ArrayList<>();
				list.add(HEALTH_ADD);
				list.add(ARMOR_ADD);
				list.add(ATK_ADD);
				list.add(ATK_MULT);
				list.add(BOW_ADD);

				list.add(TOUGH_ADD);
				list.add(REACH_ADD);

				list.add(CR_ADD);
				list.add(CD_ADD);
				list.add(ATK_SPEED_MULT);
				list.add(SPEED_MULT);

				addSlotStat(map, ArtifactTypeRegistry.SLOT_HEAD.get(), list, all);
			}
		}

		// Set Effect Config

		for (SetEntry<?> set : L2Artifacts.REGISTRATE.SET_LIST) {
			addArtifactSet(map, set.get(), set.builder);
		}

		// linear function handle

		for (var key : L2Artifacts.REGISTRATE.LINEAR_LIST.keySet()) {
			LinearFuncConfig config = new LinearFuncConfig();
			var list = L2Artifacts.REGISTRATE.LINEAR_LIST.get(key);
			for (var entry : list) {
				config.map.put(entry.get(), new LinearFuncConfig.Entry(entry.base, entry.slope));
			}
			map.add(NetworkManager.LINEAR, key, config);
		}
	}

	public static void addSlotStat(Collector map, ArtifactSlot slot, ArrayList<ResourceLocation> main, ArrayList<ResourceLocation> sub) {
		SlotStatConfig config = new SlotStatConfig();
		ResourceLocation rl = Objects.requireNonNull(slot.getRegistryName());
		config.available_main_stats.put(slot, main);
		config.available_sub_stats.put(slot, sub);
		map.add(NetworkManager.SLOT_STATS, rl, config);
	}

	private static void regStat(Collector map, ResourceLocation id, Attribute attr, AttributeModifier.Operation op, boolean perc, double base) {
		map.add(NetworkManager.STAT_TYPES, id, genEntry(attr, op, perc, base, 0.2, 2));
	}

	private static void addArtifactSet(Collector map, ArtifactSet set, Consumer<ArtifactSetConfig.SetBuilder> builder) {
		ResourceLocation rl = Objects.requireNonNull(set.getRegistryName());
		map.add(NetworkManager.ARTIFACT_SETS, rl, ArtifactSetConfig.construct(set, builder));
	}

	private static StatTypeConfig genEntry(Attribute attr, AttributeModifier.Operation op, boolean perc, double base, double sub, double factor) {
		StatTypeConfig entry = new StatTypeConfig();
		entry.base = base;
		entry.base_low = 1;
		entry.base_high = factor;
		entry.main_low = sub;
		entry.main_high = sub * factor;
		entry.sub_low = sub;
		entry.sub_high = sub * factor;
		entry.attr = attr;
		entry.op = op;
		entry.usePercent = perc;
		return entry;
	}


}