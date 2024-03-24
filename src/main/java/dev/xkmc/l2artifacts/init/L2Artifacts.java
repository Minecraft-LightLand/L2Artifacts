package dev.xkmc.l2artifacts.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2artifacts.events.ArtifactAttackListener;
import dev.xkmc.l2artifacts.events.ArtifactSel;
import dev.xkmc.l2artifacts.events.ArtifactSlotClickListener;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.data.ConfigGen;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.data.RecipeGen;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.data.slot.SlotGen;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2itemselector.select.SelectionRegistry;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import dev.xkmc.l2serial.serialization.custom_handler.RLClassHandler;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Artifacts.MODID)
@Mod.EventBusSubscriber(modid = L2Artifacts.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Artifacts {

	public static final String MODID = "l2artifacts";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();
	public static final ArtifactSlotClickListener CLICK = new ArtifactSlotClickListener();

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
	public static final RegistryObject<ParticleType<?>> WATER1 = PARTICLES.register("water1", () -> new SimpleParticleType(true));

	public L2Artifacts() {
		ArtifactTypeRegistry.register();
		ArtifactItemRegistry.register();
		ArtifactMenuRegistry.register();
		ArtifactConfig.init();
		NetworkManager.register();
		ConfigGen.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		SelectionRegistry.register(-5000, ArtifactSel.INSTANCE);
		AttackEventHandler.register(3000, new ArtifactAttackListener());
		PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new ConfigGen(event.getGenerator()));
		event.getGenerator().addProvider(event.includeServer(), new SlotGen(event.getGenerator()));
		event.getGenerator().addProvider(event.includeServer(), new ArtifactGLMProvider(event.getGenerator()));
	}

}
