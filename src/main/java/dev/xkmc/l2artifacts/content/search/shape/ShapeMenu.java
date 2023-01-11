package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactItemRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2library.base.menu.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ShapeMenu extends BaseContainerMenu<ShapeMenu> implements IFilterMenu {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "shape");

	public static ShapeMenu fromNetwork(MenuType<ShapeMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new ShapeMenu(wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	public final ArtifactChestToken token;
	public final Player player;

	public ShapeMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_UPGRADE.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(15, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("output", e -> false);
		addSlot("artifact_main", e -> e.getItem() instanceof BaseArtifact);
		addSlot("boost_main", e -> !getMainItem().isEmpty() &&
						e.getItem() instanceof UpgradeBoostItem boost &&
						boost.rank == ((BaseArtifact) getMainItem().getItem()).rank &&
						boost.type == Upgrade.Type.BOOST_MAIN_STAT,
				s -> s.setInputLockPred(this::mainSlotsUnlocked));
		addSlot("artifact_sub", this::isAllowedAsSubArtifact,
				(i, e) -> e.setInputLockPred(() -> subArtifactSlotUnlocked(i)));
		addSlot("stat_sub", (i, e) -> {
					ItemStack sub = getAsPredSlot("artifact_sub", i, 0).getItem();
					if (sub.isEmpty()) return false;
					BaseArtifact item = (BaseArtifact) sub.getItem();
					return e.getItem() == ArtifactItemRegistry.ITEM_STAT[item.rank - 1].get();
				},
				(i, s) -> s.setInputLockPred(() -> subMatSlotUnlocked(i)));
		addSlot("boost_sub", (i, e) -> {
					ItemStack sub = getAsPredSlot("artifact_sub", i, 0).getItem();
					if (sub.isEmpty()) return false;
					BaseArtifact item = (BaseArtifact) sub.getItem();
					return e.getItem() == ArtifactItemRegistry.ITEM_BOOST_SUB[item.rank - 1].get();
				},
				(i, s) -> s.setInputLockPred(() -> subMatSlotUnlocked(i)));
	}

	private ItemStack getMainItem() {
		return getAsPredSlot("artifact_main").getItem();
	}

	private boolean mainSlotsUnlocked() {
		return !getMainItem().isEmpty() && BaseArtifact.getStats(getMainItem()).isPresent();
	}

	private boolean subArtifactSlotUnlocked(int i) {
		if (!mainSlotsUnlocked()) return false;
		BaseArtifact mainItem = (BaseArtifact) getMainItem().getItem();
		int rank = mainItem.rank;
		return i < rank - 1;
	}

	private boolean subMatSlotUnlocked(int i) {
		if (!subArtifactSlotUnlocked(i)) return false;
		return !getAsPredSlot("artifact_sub", i, 0).getItem().isEmpty();
	}

	private boolean isAllowedAsSubArtifact(int index, ItemStack e) {
		ItemStack main = getMainItem();
		if (main.isEmpty()) return false;
		BaseArtifact mainItem = (BaseArtifact) getMainItem().getItem();
		if (e.getItem() != mainItem) return false;
		int rank = mainItem.rank;
		if (index >= rank - 1) return false;
		var mainOpt = BaseArtifact.getStats(main);
		if (mainOpt.isEmpty()) return false;
		var mainType = mainOpt.get().main_stat.type;
		var eOpt = BaseArtifact.getStats(e);
		if (eOpt.isEmpty()) return false;
		var eType = eOpt.get().main_stat.type;
		if (mainType == eType) return false;
		for (int i = 0; i < rank - 1; i++) {
			if (i == index) continue;
			ItemStack other = getSlot("artifact_sub", i, 0).getItem();
			if (other.isEmpty()) continue;
			var subOpt = BaseArtifact.getStats(other);
			assert subOpt.isPresent();
			var subType = subOpt.get().main_stat.type;
			if (subType == eType) return false;
		}
		return true;
	}

	@Override
	public void slotsChanged(Container cont) {
		if (player.level.isClientSide) {
			super.slotsChanged(cont);
			return;
		}
		getAsPredSlot("artifact_main").clearDirty(() -> {
			if (!getMainItem().isEmpty()) {
				BaseArtifact artifact = (BaseArtifact) getMainItem().getItem();
				int rank = artifact.rank;
				for (int i = 0; i < 4; i++) {
					var art = getAsPredSlot("artifact_sub", i, 0);
					var stat = getAsPredSlot("stat_sub", i, 0);
					var boost = getAsPredSlot("boost_sub", i, 0);
					if (!art.getItem().isEmpty()) {
						if (i >= rank - 1 || ((BaseArtifact) art.getItem().getItem()).rank != rank) {
							art.clearSlot(player);
						}
					}
					if (!stat.getItem().isEmpty()) {
						if (i >= rank - 1 || ((StatContainerItem) stat.getItem().getItem()).rank != rank) {
							stat.clearSlot(player);
						}
					}
					if (!boost.getItem().isEmpty()) {
						if (i >= rank - 1 || ((UpgradeBoostItem) boost.getItem().getItem()).rank != rank) {
							boost.clearSlot(player);
						}
					}
				}


			} else {
				clearContainerFiltered(player, cont);
			}
		});
		super.slotsChanged(cont);
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		if (data == 0) {
		}
		return false;
	}

	@Override
	protected boolean shouldClear(Container container, int slot) {
		return slot != 0;
	}

}
