package dev.xkmc.l2artifacts.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootModifier;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class ArtifactTypeRegistry {

	public static final L2Registrate.RegistryInstance<ArtifactSlot> SLOT = REGISTRATE.newRegistry("slot", ArtifactSlot.class);
	public static final L2Registrate.RegistryInstance<ArtifactSet> SET = REGISTRATE.newRegistry("set", ArtifactSet.class);
	public static final L2Registrate.RegistryInstance<SetEffect> SET_EFFECT = REGISTRATE.newRegistry("set_effect", SetEffect.class);

	public static final L2Registrate.RegistryInstance<LinearFuncHandle> LINEAR = REGISTRATE.newRegistry("linear", LinearFuncHandle.class);

	public static final RegistryEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", () -> new ArtifactSlot(ArtifactSlotCuriosType.HEAD));
	public static final RegistryEntry<ArtifactSlot> SLOT_NECKLACE = regSlot("necklace", () -> new ArtifactSlot(ArtifactSlotCuriosType.NECKLACE));
	public static final RegistryEntry<ArtifactSlot> SLOT_BRACELET = regSlot("bracelet", () -> new ArtifactSlot(ArtifactSlotCuriosType.BRACELET));
	public static final RegistryEntry<ArtifactSlot> SLOT_BODY = regSlot("body", () -> new ArtifactSlot(ArtifactSlotCuriosType.BODY));
	public static final RegistryEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", () -> new ArtifactSlot(ArtifactSlotCuriosType.BELT));

	public static final RegistryEntry<Codec<ArtifactLootModifier>> SER = REGISTRATE.simple("main", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ArtifactLootModifier.CODEC);

	public static void register() {

	}

	private static RegistryEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
		return REGISTRATE.generic(SLOT, id, slot).defaultLang().register();
	}

}
