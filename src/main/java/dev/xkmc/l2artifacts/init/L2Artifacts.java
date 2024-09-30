package dev.xkmc.l2artifacts.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2artifacts.events.ArtifactAttackListener;
import dev.xkmc.l2artifacts.events.ArtifactSel;
import dev.xkmc.l2artifacts.events.ArtifactSlotClickListener;
import dev.xkmc.l2artifacts.init.data.*;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootGen;
import dev.xkmc.l2artifacts.init.data.slot.SlotGen;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2itemselector.select.SelectionRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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
	public static final ArtifactSlotClickListener CLICK = new ArtifactSlotClickListener();

	public L2Artifacts() {
		ArtifactTypeRegistry.register();
		ArtifactItems.register();
		ArtifactMenuRegistry.register();
		ArtifactEffects.register();
		ArtifactConfig.init();
		NetworkManager.register();
		ConfigGen.register();

		SelectionRegistry.register(-5000, ArtifactSel.INSTANCE);
		AttackEventHandler.register(3000, new ArtifactAttackListener());
	}

	@SubscribeEvent
	public static void commonInit(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, ArtifactLootGen::onLootGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, ArtifactTagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, ArtifactTagGen::onEntityTypeGen);

		var run = event.includeServer();
		var gen = event.getGenerator();
		var reg = event.getLookupProvider();
		var out = gen.getPackOutput();

		gen.addProvider(run, new ConfigGen(gen));
		gen.addProvider(run, new SlotGen(event.getGenerator()));
		gen.addProvider(run, new ArtifactGLMProvider(out, reg));
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
