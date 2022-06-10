package dev.xkmc.l2artifacts.init.registrate;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2library.serial.handler.RLClassHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_TOTAL;

public class ArtifactRegistry {

	public static IForgeRegistry<ArtifactSlot> SLOT;
	public static IForgeRegistry<ArtifactStatType> STAT_TYPE;
	public static IForgeRegistry<ArtifactSet> SET;

	public static RegistryEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", ArtifactSlot::new);
	public static RegistryEntry<ArtifactSlot> SLOT_NECKLACE = regSlot("necklace", ArtifactSlot::new);
	public static RegistryEntry<ArtifactSlot> SLOT_BRACELET = regSlot("bracelet", ArtifactSlot::new);
	public static RegistryEntry<ArtifactSlot> SLOT_BODY = regSlot("body", ArtifactSlot::new);
	public static RegistryEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", ArtifactSlot::new);

	public static RegistryEntry<ArtifactStatType> HEALTH_ADD = regStat("health_add", () -> Attributes.MAX_HEALTH, ADDITION);
	public static RegistryEntry<ArtifactStatType> ARMOR_ADD = regStat("armor_add", () -> Attributes.ARMOR, ADDITION);
	public static RegistryEntry<ArtifactStatType> TOUGH_ADD = regStat("tough_add", () -> Attributes.ARMOR_TOUGHNESS, ADDITION);
	public static RegistryEntry<ArtifactStatType> ATK_ADD = regStat("attack_add", () -> Attributes.ATTACK_DAMAGE, ADDITION);
	public static RegistryEntry<ArtifactStatType> ATK_MULT = regStat("attack_mult", () -> Attributes.ATTACK_DAMAGE, MULTIPLY_TOTAL);
	public static RegistryEntry<ArtifactStatType> SPEED_MULT = regStat("speed_mult", () -> Attributes.MOVEMENT_SPEED, MULTIPLY_TOTAL);
	public static RegistryEntry<ArtifactStatType> ATK_SPEED_MULT = regStat("attack_speed_mult", () -> Attributes.ATTACK_SPEED, MULTIPLY_TOTAL);

	@SuppressWarnings({"unchecked"})
	public static void createRegistries(NewRegistryEvent event) {
		event.create(new RegistryBuilder<ArtifactSlot>()
				.setName(new ResourceLocation(L2Artifacts.MODID, "slot"))
				.setType(ArtifactSlot.class), e -> SLOT = regSerializer(e));

		event.create(new RegistryBuilder<ArtifactStatType>()
				.setName(new ResourceLocation(L2Artifacts.MODID, "stat_type"))
				.setType(ArtifactStatType.class), e -> STAT_TYPE = regSerializer(e));

		event.create(new RegistryBuilder<ArtifactSet>()
				.setName(new ResourceLocation(L2Artifacts.MODID, "set"))
				.setType(ArtifactSet.class), e -> SET = regSerializer(e));
	}

	@SuppressWarnings({"rawtypes"})
	private static <T extends IForgeRegistryEntry<T>> IForgeRegistry regSerializer(IForgeRegistry<T> r) {
		new RLClassHandler<>(r.getRegistrySuperType(), () -> r);
		return r;
	}

	private static RegistryEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
		return REGISTRATE.generic(ArtifactSlot.class, id, slot).defaultLang().register();
	}

	private static RegistryEntry<ArtifactStatType> regStat(String id, Supplier<Attribute> attr, AttributeModifier.Operation op) {
		return REGISTRATE.generic(ArtifactStatType.class, id, () -> new ArtifactStatType(attr.get(), op)).defaultLang().register();
	}

	private static RegistryEntry<ArtifactSet> regSet(String id, NonNullSupplier<ArtifactSet> set) {
		return REGISTRATE.generic(ArtifactSet.class, id, set).defaultLang().register();
	}

}
