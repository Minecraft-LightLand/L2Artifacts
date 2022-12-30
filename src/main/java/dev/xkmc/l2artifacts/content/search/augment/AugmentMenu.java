package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class AugmentMenu extends BaseContainerMenu<AugmentMenu> implements IFilterMenu {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "augment");

	public static AugmentMenu fromNetwork(MenuType<AugmentMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new AugmentMenu(wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	public final ArtifactChestToken token;
	public final Player player;

	public final DataSlot experience;
	public final DataSlot exp_cost;
	public final DataSlot player_cost;
	public final DataSlot mask;

	public AugmentMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_AUGMENT.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(4, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("input", e -> e.getItem() instanceof BaseArtifact);
		addSlot("in_0", e -> this.container.getItem(0).getItem() instanceof BaseArtifact base &&
				e.getItem() == ArtifactItemRegistry.ITEM_STAT[base.rank - 1].get());
		addSlot("in_1", e -> this.container.getItem(0).getItem() instanceof BaseArtifact base &&
				e.getItem() == ArtifactItemRegistry.ITEM_BOOST_MAIN[base.rank - 1].get());
		addSlot("in_2", e -> this.container.getItem(0).getItem() instanceof BaseArtifact base &&
				e.getItem() == ArtifactItemRegistry.ITEM_BOOST_SUB[base.rank - 1].get());
		this.experience = addDataSlot(DataSlot.standalone());
		this.exp_cost = addDataSlot(DataSlot.standalone());
		this.player_cost = addDataSlot(DataSlot.standalone());
		this.mask = addDataSlot(DataSlot.standalone());
		experience.set(token.exp);
	}

	@Override
	public void slotsChanged(Container cont) {
		if (player.level.isClientSide) return;
		ItemStack stack = cont.getItem(0);
		int ec = 0;
		int pc = 0;
		boolean useStat = false;
		boolean useSub = false;
		boolean useMain = false;
		if (!stack.isEmpty()) {
			BaseArtifact item = (BaseArtifact) stack.getItem();
			var result = item.resolve(stack, false, player.getRandom());
			if (result.getResult().consumesAction()) {
				stack = result.getObject();
				cont.setItem(0, stack);
			}
			var opt = BaseArtifact.getStats(stack);
			if (opt.isPresent()) {
				var stats = opt.get();
				if (stats.level < ArtifactUpgradeManager.getMaxLevel(item.rank)) {
					ec = ArtifactUpgradeManager.getExpForLevel(item.rank, stats.level) - stats.exp;
					pc = (int) Math.ceil(Math.log10(ec) * item.rank);
					useMain = !container.getItem(2).isEmpty();
					if ((stats.level + 1) % ModConfig.COMMON.levelPerSubStat.get() == 0) {
						useSub = !container.getItem(3).isEmpty();
						ItemStack stat = cont.getItem(1);
						var opt_stat = StatContainerItem.getType(stat);
						if (opt_stat.isPresent()) {
							var astat = opt_stat.get();
							if (stats.main_stat.type != astat && stats.map.containsKey(astat)) {
								useStat = true;
							}
						}
					}
				}
			}
		} else {
			clearContainer(player, cont);
		}
		exp_cost.set(ec);
		player_cost.set(pc);
		mask.set((useStat ? 1 : 0) + (useMain ? 2 : 0) + (useSub ? 4 : 0));
		super.slotsChanged(cont);
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		if (data == 0) {
			boolean canUpgrade = player_cost.get() > 0 && exp_cost.get() <= experience.get() &&
					(player.getAbilities().instabuild || player.experienceLevel >= player_cost.get());
			if (player.level.isClientSide) {
				return canUpgrade;
			}
			if (!canUpgrade)
				return false;
			player.giveExperienceLevels(-player_cost.get());
			ItemStack stack = container.getItem(0);
			Upgrade upgrade = BaseArtifact.getUpgrade(stack).orElseGet(Upgrade::new);
			int mask = this.mask.get();
			if ((mask & 1) > 0) {
				ItemStack stat = container.getItem(1);
				var opt_stat = StatContainerItem.getType(stat);
				opt_stat.ifPresent(artifactStatType -> upgrade.stats.add(artifactStatType));
				container.getItem(1).shrink(1);
			}
			if ((mask & 2) > 0) {
				upgrade.main++;
				container.getItem(2).shrink(1);
			}
			if ((mask & 4) > 0) {
				upgrade.sub++;
				container.getItem(3).shrink(1);
			}
			BaseArtifact.setUpgrade(stack, upgrade);
			BaseArtifact.upgrade(stack, exp_cost.get(), player.getRandom());
			stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
			container.setItem(0, stack);
			costExp(exp_cost.get());
		}
		return false;
	}

	private void costExp(int exp) {
		token.exp -= exp;
		ArtifactChestItem.setExp(token.stack, token.exp);
		experience.set(token.exp);
		sendAllDataToRemote();
	}

}
