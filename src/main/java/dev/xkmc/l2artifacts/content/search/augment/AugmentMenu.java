package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.tab.ArtifactTabData;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2core.base.menu.base.PredSlot;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2core.base.menu.data.IntDataSlot;
import dev.xkmc.l2library.util.GenericItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class AugmentMenu extends BaseContainerMenu<AugmentMenu> implements IFilterMenu {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "augment");

	public static AugmentMenu fromNetwork(MenuType<AugmentMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		return new AugmentMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
	}

	public final ArtifactTabData token;
	public final Player player;

	public final IntDataSlot experience;
	public final IntDataSlot exp_cost;
	public final IntDataSlot mask;

	private final PredSlot input, in_0, in_1, in_2;

	public AugmentMenu(int wid, Inventory plInv, ArtifactTabData token) {
		super(ArtifactMenuRegistry.MT_AUGMENT.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(4, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("input", e -> e.getItem() instanceof BaseArtifact);
		addSlot("in_0", e -> e.getItem() == ArtifactItems.ITEM_STAT[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		addSlot("in_1", e -> e.getItem() == ArtifactItems.ITEM_BOOST_MAIN[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		addSlot("in_2", e -> e.getItem() == ArtifactItems.ITEM_BOOST_SUB[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		this.experience = new IntDataSlot(this);
		this.exp_cost = new IntDataSlot(this);
		this.mask = new IntDataSlot(this);
		experience.set(token.token.exp);

		this.input = getAsPredSlot("input");
		this.in_0 = getAsPredSlot("in_0");
		this.in_1 = getAsPredSlot("in_1");
		this.in_2 = getAsPredSlot("in_2");
	}

	private GenericItemStack<BaseArtifact> getMainItem() {
		return GenericItemStack.of(this.input.getItem());
	}

	private boolean isSlotLocked() {
		return this.input.getItem().isEmpty();
	}

	@Override
	protected void securedServerSlotChange(Container cont) {
		ItemStack stack = input.getItem();
		int ec = 0;
		boolean useStat = false;
		boolean useSub = false;
		boolean useMain = false;
		if (!stack.isEmpty()) {
			BaseArtifact item = getMainItem().item();
			var result = item.resolve(stack, false, player.getRandom());
			if (result.getResult().consumesAction()) {
				stack = result.getObject();
				input.set(stack);
			}
		}
		in_0.updateEject(player);
		in_1.updateEject(player);
		in_2.updateEject(player);
		if (!stack.isEmpty()) {
			BaseArtifact item = getMainItem().item();
			var opt = BaseArtifact.getStats(stack);
			if (opt.isPresent()) {
				var stats = opt.get();
				if (stats.level() < ArtifactUpgradeManager.getMaxLevel(item.rank)) {
					ec = ArtifactUpgradeManager.getExpForLevel(item.rank, stats.level()) - stats.exp();
					useMain = !in_1.getItem().isEmpty();
					if ((stats.level() + 1) % ArtifactConfig.COMMON.levelPerSubStat.get() == 0) {
						useSub = !in_2.getItem().isEmpty();
						ItemStack stat = in_0.getItem();
						var opt_stat = StatContainerItem.getType(access, stat);
						if (opt_stat.isPresent()) {
							var astat = opt_stat.get();
							if (!stats.main_stat().type.equals(astat) && stats.containsKey(astat)) {
								useStat = true;
							}
						}
					}
				}
			}
		}
		exp_cost.set(ec);
		mask.set((useStat ? 1 : 0) + (useMain ? 2 : 0) + (useSub ? 4 : 0));
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		if (data == 0) {
			int cost = exp_cost.get();
			boolean canUpgrade = cost > 0 && cost <= experience.get();
			if (player.level().isClientSide) {
				return canUpgrade;
			}
			if (!canUpgrade)
				return false;
			ItemStack stack = getAsPredSlot("input").getItem();
			var upgrade = BaseArtifact.getUpgrade(stack).orElse(Upgrade.EMPTY).mutable();
			int mask = this.mask.get();
			if ((mask & 1) > 0) {
				ItemStack stat = getAsPredSlot("in_0").getItem();
				var opt_stat = StatContainerItem.getType(access, stat);
				opt_stat.ifPresent(upgrade::add);
				getAsPredSlot("in_0").getItem().shrink(1);
			}
			if ((mask & 2) > 0) {
				upgrade.addMain();
				getAsPredSlot("in_1").getItem().shrink(1);
			}
			if ((mask & 4) > 0) {
				upgrade.addSub();
				getAsPredSlot("in_2").getItem().shrink(1);
			}
			ArtifactItems.UPGRADES.set(stack, upgrade.immutable());
			BaseArtifact.upgrade(stack, cost);
			stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
			getAsPredSlot("input").set(stack);
			costExp(cost);
		}
		return false;
	}

	private void costExp(int exp) {
		token.token.exp -= exp;
		ArtifactChestItem.setExp(token.token.stack, token.token.exp);
		experience.set(token.token.exp);
		sendAllDataToRemote();
	}

}
