package dev.xkmc.l2artifacts.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2artifacts.events.ArtifactEffectEvents;
import dev.xkmc.l2artifacts.events.CommonEvents;
import dev.xkmc.l2artifacts.events.CraftEvents;
import dev.xkmc.l2artifacts.events.CritHandler;
import dev.xkmc.l2artifacts.init.data.*;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.init.events.attack.AttackEventHandler;
import dev.xkmc.l2library.serial.handler.RLClassHandler;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("l2artifacts")
public class L2Artifacts {

	public static final String MODID = "l2artifacts";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();

	private static void registerRegistrates(IEventBus bus) {
		ArtifactTypeRegistry.register();
		ArtifactItemRegistry.register();
		ArtifactMenuRegistry.register();
		ModConfig.init();
		NetworkManager.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		new RLClassHandler<>(Attribute.class, () -> ForgeRegistries.ATTRIBUTES);
	}

	private static void registerForgeEvents() {
		AttackEventHandler.LISTENERS.put(3000, new CritHandler());
		MinecraftForge.EVENT_BUS.register(CommonEvents.class);
		MinecraftForge.EVENT_BUS.register(CraftEvents.class);
		MinecraftForge.EVENT_BUS.register(CritHandler.class);
		MinecraftForge.EVENT_BUS.register(ArtifactEffectEvents.class);
	}

	private static void registerModBusEvents(IEventBus bus) {
		bus.addListener(L2Artifacts::modifyAttributes);
		bus.addListener(L2Artifacts::setup);
		bus.addListener(L2Artifacts::gatherData);
		bus.addListener(L2Artifacts::sendMessage);
	}

	public L2Artifacts() {
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		registerModBusEvents(bus);
		registerRegistrates(bus);
		registerForgeEvents();
	}

	private static void modifyAttributes(EntityAttributeModificationEvent event) {
	}

	private static void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	public static void gatherData(GatherDataEvent event) {
		event.getGenerator().addProvider(event.includeServer(), new ConfigGen(event.getGenerator()));
		event.getGenerator().addProvider(event.includeServer(), new ArtifactGLMProvider(event.getGenerator()));
	}

	private static void sendMessage(final InterModEnqueueEvent event) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> ArtifactSlotCuriosType.HEAD.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> ArtifactSlotCuriosType.NECKLACE.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> ArtifactSlotCuriosType.BRACELET.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> ArtifactSlotCuriosType.BODY.getMessageBuilder().build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> ArtifactSlotCuriosType.BELT.getMessageBuilder().build());
	}

}
