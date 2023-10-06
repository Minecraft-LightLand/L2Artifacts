package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.common.IntDataSlot;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ModConfig;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.PredSlot;
import dev.xkmc.l2library.base.menu.SpriteManager;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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

	public final IntDataSlot experience;
	public final IntDataSlot exp_cost;
	public final IntDataSlot mask;

	private final PredSlot input, in_0, in_1, in_2;

	public AugmentMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_AUGMENT.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(4, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("input", e -> e.getItem() instanceof BaseArtifact);
		addSlot("in_0", e -> e.getItem() == ArtifactItemRegistry.ITEM_STAT[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		addSlot("in_1", e -> e.getItem() == ArtifactItemRegistry.ITEM_BOOST_MAIN[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		addSlot("in_2", e -> e.getItem() == ArtifactItemRegistry.ITEM_BOOST_SUB[getMainItem().item().rank - 1].get(),
				e -> e.setInputLockPred(this::isSlotLocked));
		this.experience = new IntDataSlot(this);
		this.exp_cost = new IntDataSlot(this);
		this.mask = new IntDataSlot(this);
		experience.set(token.exp);

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
		int pc = 0;
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
				if (stats.level < ArtifactUpgradeManager.getMaxLevel(item.rank)) {
					ec = ArtifactUpgradeManager.getExpForLevel(item.rank, stats.level) - stats.exp;
					pc = (int) Math.ceil(Math.log10(ec) * item.rank);
					useMain = !in_1.getItem().isEmpty();
					if ((stats.level + 1) % ModConfig.COMMON.levelPerSubStat.get() == 0) {
						useSub = !in_2.getItem().isEmpty();
						ItemStack stat = in_0.getItem();
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
		}
		exp_cost.set(ec);
		mask.set((useStat ? 1 : 0) + (useMain ? 2 : 0) + (useSub ? 4 : 0));
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		if (data == 0) {
			int cost = exp_cost.get();
			boolean canUpgrade = cost > 0 && cost <= experience.get();
			if (player.level.isClientSide) {
				return canUpgrade;
			}
			if (!canUpgrade)
				return false;
			ItemStack stack = getAsPredSlot("input").getItem();
			Upgrade upgrade = BaseArtifact.getUpgrade(stack).orElseGet(Upgrade::new);
			int mask = this.mask.get();
			if ((mask & 1) > 0) {
				ItemStack stat = getAsPredSlot("in_0").getItem();
				var opt_stat = StatContainerItem.getType(stat);
				opt_stat.ifPresent(artifactStatType -> upgrade.stats.add(artifactStatType));
				getAsPredSlot("in_0").getItem().shrink(1);
			}
			if ((mask & 2) > 0) {
				upgrade.main++;
				getAsPredSlot("in_1").getItem().shrink(1);
			}
			if ((mask & 4) > 0) {
				upgrade.sub++;
				getAsPredSlot("in_2").getItem().shrink(1);
			}
			BaseArtifact.setUpgrade(stack, upgrade);
			BaseArtifact.upgrade(stack, cost, player.getRandom());
			stack = ((BaseArtifact) stack.getItem()).resolve(stack, false, player.getRandom()).getObject();
			getAsPredSlot("input").set(stack);
			costExp(cost);
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
