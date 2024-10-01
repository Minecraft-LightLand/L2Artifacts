package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.config.SetGroup;
import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.misc.*;
import dev.xkmc.l2artifacts.content.swap.ArtifactSwapItem;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import static dev.xkmc.l2artifacts.init.L2Artifacts.REGISTRATE;

@SuppressWarnings({"raw_type", "unchecked"})
public class ArtifactItems {

	public static final String[] RANK_NAME = {" -Common-", " =Rare=", " >Epic<", " »Legendary«", " -»Godly«-"};

	static {
		REGISTRATE.buildL2CreativeTab("artifacts", "L2 Artifacts", b -> b
				.icon(ArtifactItems.SELECT::asStack));
	}

	public static final ItemEntry<SelectArtifactItem> SELECT;
	public static final ItemEntry<ArtifactChestItem> FILTER, UPGRADED_POCKET;
	public static final ItemEntry<ArtifactSwapItem> SWAP;
	public static final ItemEntry<RandomArtifactItem>[] RANDOM;
	public static final ItemEntry<RandomArtifactSetItem>[] RANDOM_SET;
	public static final ItemEntry<ExpItem>[] ITEM_EXP;
	public static final ItemEntry<StatContainerItem>[] ITEM_STAT;
	public static final ItemEntry<UpgradeBoostItem>[] ITEM_BOOST_MAIN, ITEM_BOOST_SUB;

	private static final DCReg DC = DCReg.of(L2Artifacts.REG);
	public static final DCVal<ArtifactStats> STATS = DC.reg("stats", ArtifactStats.class, false);
	public static final DCVal<Upgrade> UPGRADES = DC.reg("upgrades", Upgrade.class, false);
	public static final DCVal<String> STAT = DC.str("stat");
	public static final DCVal<ItemContainerContents> ITEMS = DC.reg("items", ItemContainerContents.CODEC, ItemContainerContents.STREAM_CODEC, false);
	public static final DCVal<Integer> EXP = DC.intVal("experience");
	public static final DCVal<SetGroup> GROUP = DC.reg("set_group", SetGroup.class, true);

	static {
		SELECT = REGISTRATE.item("select", SelectArtifactItem::new)
				.defaultModel().lang("Artifact Selector (Creative)").register();
		FILTER = REGISTRATE.item("filter", ArtifactChestItem::new)
				.defaultModel().lang("Artifact Pocket").register();
		SWAP = REGISTRATE.item("swap", ArtifactSwapItem::new)
				.defaultModel().lang("Artifact Quick Swap").register();
		UPGRADED_POCKET = REGISTRATE.item("upgraded_pocket", ArtifactChestItem::new)
				.defaultModel().lang("Upgraded Artifact Pocket").register();
		int n = 5;
		RANDOM = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			TagKey<Item> artifact = ItemTags.create(L2Artifacts.loc("artifact"));
			TagKey<Item> rank_tag = ItemTags.create(L2Artifacts.loc("rank_" + r));
			RANDOM[i] = REGISTRATE.item("random_" + r, p -> new RandomArtifactItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/random")))
					.tag(rank_tag, artifact)
					.lang("Random Artifact" + RANK_NAME[i]).register();
		}
		RANDOM_SET = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			RANDOM_SET[i] = REGISTRATE.item("random_set_" + r, p -> new RandomArtifactSetItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/random_set")))
					.lang("Random Artifact Set" + RANK_NAME[i]).register();
		}
		ITEM_EXP = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_EXP[i] = REGISTRATE.item("artifact_experience_" + r, p -> new ExpItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/artifact_experience")))
					.lang("Artifact Experience" + RANK_NAME[i]).register();
		}
		ITEM_STAT = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_STAT[i] = REGISTRATE.item("stat_container_" + r, p -> new StatContainerItem(p, r))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/stat_container")))
					.lang("Stat Container" + RANK_NAME[i]).register();
		}
		ITEM_BOOST_MAIN = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_BOOST_MAIN[i] = REGISTRATE.item("boost_main_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_MAIN_STAT))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/boost_main")))
					.lang("Main Stat Booster" + RANK_NAME[i]).register();
		}
		ITEM_BOOST_SUB = new ItemEntry[n];
		for (int i = 0; i < n; i++) {
			int r = i + 1;
			ITEM_BOOST_SUB[i] = REGISTRATE.item("boost_sub_" + r, p -> new UpgradeBoostItem(p, r, Upgrade.Type.BOOST_SUB_STAT))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("item/generated"))
							.texture("layer0", L2Artifacts.loc("item/rank/" + r))
							.texture("layer1", L2Artifacts.loc("item/boost_sub")))
					.lang("Sub Stat Booster" + RANK_NAME[i]).register();
		}
	}

	static {
		LAItem0.register();
		LAItem1.register();
		LAItem2.register();
		LAItem3.register();
		LAItem4.register();
		LAItem5.register();
		LAItem6.register();
		LAItemMisc.register();
	}

	public static void register() {

	}

}