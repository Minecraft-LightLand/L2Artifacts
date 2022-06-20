package dev.xkmc.l2artifacts.init;

import dev.xkmc.l2artifacts.events.ArtifactEffectEvents;
import dev.xkmc.l2artifacts.events.CommonEvents;
import dev.xkmc.l2artifacts.events.CraftEvents;
import dev.xkmc.l2artifacts.events.CritHandler;
import dev.xkmc.l2artifacts.init.data.*;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistry;
import dev.xkmc.l2library.init.events.AttackEventHandler;
import dev.xkmc.l2library.menu.tabs.contents.AttributeEntry;
import dev.xkmc.l2library.menu.tabs.contents.AttributeScreen;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("l2artifacts")
public class L2Artifacts {

	public static final String MODID = "l2artifacts";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();

	private static void registerRegistrates(IEventBus bus) {
		ArtifactRegistry.register();
		ArtifactItemRegistry.register();
		ModConfig.init();
		NetworkManager.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
	}

	private static void registerForgeEvents() {
		AttackEventHandler.LISTENERS.add(new CritHandler());
		MinecraftForge.EVENT_BUS.register(CommonEvents.class);
		MinecraftForge.EVENT_BUS.register(CraftEvents.class);
		MinecraftForge.EVENT_BUS.register(CritHandler.class);
		MinecraftForge.EVENT_BUS.register(ArtifactEffectEvents.class);
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(ArtifactRegistry::createRegistries);
		bus.addListener(L2Artifacts::modifyAttributes);
		bus.addListener(L2Artifacts::setup);
		bus.addListener(L2Artifacts::gatherData);
		bus.addListener(L2Artifacts::sendMessage);
	}

	public L2Artifacts() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModClient.onCtorClient(bus, MinecraftForge.EVENT_BUS));
		registerRegistrates(bus);
		registerForgeEvents();
	}

	private static void modifyAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, ArtifactRegistry.CRIT_RATE.get());
		event.add(EntityType.PLAYER, ArtifactRegistry.CRIT_DMG.get());
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			AttributeEntry.add(ArtifactRegistry.CRIT_RATE, true,11000);
			AttributeEntry.add(ArtifactRegistry.CRIT_DMG, true,12000);
		});
	}

	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(new ConfigGen(event.getGenerator()));
		event.getGenerator().addProvider(new ArtifactGLMProvider(event.getGenerator()));
	}

	private static void sendMessage(final InterModEnqueueEvent event) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BODY.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
	}

}
