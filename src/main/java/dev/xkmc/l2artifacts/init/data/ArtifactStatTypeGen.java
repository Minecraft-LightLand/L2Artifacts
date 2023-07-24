package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactDatapackRegistry;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.util.annotation.DataGenOnly;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static dev.xkmc.l2artifacts.init.registrate.ArtifactDatapackRegistry.STAT_TYPE;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION;
import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.MULTIPLY_BASE;

public class ArtifactStatTypeGen extends DatapackBuiltinEntriesProvider {

	private static final List<DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder>> LIST = new ArrayList<>();

	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> HEALTH_ADD = regStat("health_add", () -> Attributes.MAX_HEALTH, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ARMOR_ADD = regStat("armor_add", () -> Attributes.ARMOR, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> TOUGH_ADD = regStat("tough_add", () -> Attributes.ARMOR_TOUGHNESS, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_ADD = regStat("attack_add", () -> Attributes.ATTACK_DAMAGE, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> REACH_ADD = regStat("reach_add", ForgeMod.ENTITY_REACH, ADDITION, false);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> CR_ADD = regStat("crit_rate_add", L2DamageTracker.CRIT_RATE, ADDITION, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> CD_ADD = regStat("crit_damage_add", L2DamageTracker.CRIT_DMG, ADDITION, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_MULT = regStat("attack_mult", () -> Attributes.ATTACK_DAMAGE, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> SPEED_MULT = regStat("speed_mult", () -> Attributes.MOVEMENT_SPEED, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> ATK_SPEED_MULT = regStat("attack_speed_mult", () -> Attributes.ATTACK_SPEED, MULTIPLY_BASE, true);
	public static final DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> BOW_ADD = regStat("bow_strength_add", L2DamageTracker.BOW_STRENGTH, ADDITION, true);

	private static void createEntries(BootstapContext<ArtifactStatType> ctx) {
		for (var e : LIST) {
			ctx.register(e.key, e.sup().get());
		}
	}

	public ArtifactStatTypeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, new RegistrySetBuilder().add(STAT_TYPE.key, ArtifactStatTypeGen::createEntries), Set.of("minecraft", L2Artifacts.MODID));
	}

	private static DatapackEntry<ArtifactStatType, ArtifactStatTypeHolder> regStat(String id, Supplier<Attribute> attr, AttributeModifier.Operation op, boolean usePercent) {
		var ans = new DatapackEntry<>(STAT_TYPE, STAT_TYPE.entryKey(new ResourceLocation(L2Artifacts.MODID, id)), () -> new ArtifactStatType(attr.get(), op.name(), usePercent));
		LIST.add(ans);
		L2Artifacts.REGISTRATE.addRawLang(STAT_TYPE.key.location().getPath() + "." + L2Artifacts.MODID + "." + id, RegistrateLangProvider.toEnglishName(id));
		return ans;
	}

	public record DatapackEntry<T, R extends ArtifactDatapackRegistry.EntryHolder<T>>(
			ArtifactDatapackRegistry.DatapackInstance<T, R> reg, ResourceKey<T> key, Supplier<ArtifactStatType> sup
	) {

		@DataGenOnly
		public R get(HolderLookup.Provider registries) {
			return reg.func.apply(registries.asGetterLookup().lookupOrThrow(reg.key).getOrThrow(key()));
		}

	}


}
