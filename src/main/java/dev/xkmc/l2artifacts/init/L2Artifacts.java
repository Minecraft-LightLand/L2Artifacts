package dev.xkmc.l2artifacts.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.l2artifacts.content.client.select.ChooseArtifactToServer;
import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.search.common.OpenTabToServer;
import dev.xkmc.l2artifacts.content.search.token.FilterDataToServer;
import dev.xkmc.l2artifacts.events.ArtifactAttackListener;
import dev.xkmc.l2artifacts.events.ArtifactSlotClickListener;
import dev.xkmc.l2artifacts.init.data.*;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootGen;
import dev.xkmc.l2artifacts.init.data.slot.SlotGen;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2backpack.content.common.BaseBagItemHandler;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2core.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Artifacts.MODID)
@EventBusSubscriber(modid = L2Artifacts.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2Artifacts {

	public static final String MODID = "l2artifacts";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();

	public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(
			MODID, 1,
			e -> e.create(ChooseArtifactToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(OpenTabToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER),
			e -> e.create(FilterDataToServer.class, PacketHandler.NetDir.PLAY_TO_SERVER)
	);

	public static final ArtifactSlotClickListener CLICK = new ArtifactSlotClickListener();

	public L2Artifacts() {
		ArtifactTypeRegistry.register();
		ArtifactItems.register();
		ArtifactMenuRegistry.register();
		ArtifactEffects.register();
		ArtifactTabRegistry.register();
		ArtifactConfig.init();
		Handlers.registerReg(StatType.class, ArtifactTypeRegistry.STAT_TYPE.key());

		AttackEventHandler.register(3000, new ArtifactAttackListener());
	}

	@SubscribeEvent
	public static void commonInit(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}


	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		// items
		if (ModList.get().isLoaded(L2Backpack.MODID)) {
			event.registerItem(Capabilities.ItemHandler.ITEM, (stack, c) -> new BaseBagItemHandler(stack), ArtifactItems.SWAP);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		ConfigGen.register();
		REGISTRATE.addDataGenerator(ProviderType.LANG, ArtifactLang::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, ArtifactLootGen::onLootGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, ArtifactTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, ArtifactTagGen::onEntityTypeGen);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, L2Artifacts::onDataMapGen);
		var init = REGISTRATE.getDataGenInitializer();
		init.add(ArtifactTypeRegistry.STAT_TYPE.key(), ConfigGen::genSlotType);

		var run = event.includeServer();
		var gen = event.getGenerator();
		var reg = event.getLookupProvider();
		var out = gen.getPackOutput();
		var file = event.getExistingFileHelper();

		gen.addProvider(run, new SlotGen(MODID, out, file, reg));
		gen.addProvider(run, new ArtifactGLMProvider(out, reg));
	}

	private static void onDataMapGen(RegistrateDataMapProvider pvd) {
		ArtifactTabRegistry.genTabs(pvd);
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
