package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.core.ArtifactStats;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.content.upgrades.StatContainerItem;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.content.upgrades.UpgradeBoostItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItemRegistry;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.PredSlot;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ShapeMenu extends BaseContainerMenu<ShapeMenu> implements IFilterMenu {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "shape");

	public static ShapeMenu fromNetwork(MenuType<ShapeMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		return new ShapeMenu(wid, plInv, ArtifactChestToken.of(plInv.player, i));
	}

	public final ArtifactChestToken token;
	public final Player player;

	public ShapeMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_SHAPE.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(15, e).setMax(1), true);
		this.token = token;
		this.player = plInv.player;
		addSlot(ShapeSlots.OUTPUT.slot(), e -> false);
		addSlot(ShapeSlots.ARTIFACT_MAIN.slot(), e -> {
			if (!(e.getItem() instanceof BaseArtifact art)) return false;
			int rank = art.rank;
			var opt = BaseArtifact.getStats(e);
			if (opt.isEmpty()) return false;
			return opt.get().level == ArtifactUpgradeManager.getMaxLevel(rank);
		});
		addSlot(ShapeSlots.BOOST_MAIN.slot(), e -> !getMainItem().isEmpty() && e.getItem() instanceof UpgradeBoostItem boost && boost.rank == ((BaseArtifact) getMainItem().getItem()).rank && boost.type == Upgrade.Type.BOOST_MAIN_STAT, s -> s.setInputLockPred(this::mainSlotsLocked));
		addSlot(ShapeSlots.ARTIFACT_SUB.slot(), this::isAllowedAsSubArtifact, (i, e) -> e.setInputLockPred(() -> subArtifactSlotLocked(i)));
		addSlot(ShapeSlots.STAT_SUB.slot(), (i, e) -> {
			ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).getItem();
			if (sub.isEmpty()) return false;
			BaseArtifact item = (BaseArtifact) sub.getItem();
			if (e.getItem() != ArtifactItemRegistry.ITEM_STAT[item.rank - 1].get()) return false;
			var statOpt = StatContainerItem.getType(player.level(), e);
			var subOpt = BaseArtifact.getStats(sub);
			if (statOpt.isEmpty()) return false;
			if (subOpt.isEmpty()) return false;
			return subOpt.get().main_stat.type == statOpt.get();
		}, (i, s) -> s.setInputLockPred(() -> subMatSlotLocked(i)));
		addSlot(ShapeSlots.BOOST_SUB.slot(), (i, e) -> {
			ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).getItem();
			if (sub.isEmpty()) return false;
			BaseArtifact item = (BaseArtifact) sub.getItem();
			return e.getItem() == ArtifactItemRegistry.ITEM_BOOST_SUB[item.rank - 1].get();
		}, (i, s) -> s.setInputLockPred(() -> subMatSlotLocked(i)));
	}

	public PredSlot getAsPredSlot(ShapeSlots slot) {
		return super.getAsPredSlot(slot.slot());
	}

	public PredSlot getAsPredSlot(ShapeSlots slot, int i) {
		return super.getAsPredSlot(slot.slot(), i, 0);
	}

	private ItemStack getMainItem() {
		return ShapeSlots.ARTIFACT_MAIN.get(this).getItem();
	}

	private boolean mainSlotsLocked() {
		return getMainItem().isEmpty() || BaseArtifact.getStats(getMainItem()).isEmpty();
	}

	private boolean subArtifactSlotLocked(int i) {
		if (mainSlotsLocked()) return true;
		BaseArtifact mainItem = (BaseArtifact) getMainItem().getItem();
		int rank = mainItem.rank;
		return i >= rank - 1;
	}

	private boolean subMatSlotLocked(int i) {
		if (subArtifactSlotLocked(i)) return true;
		ItemStack sub = ShapeSlots.ARTIFACT_SUB.get(this, i).getItem();
		if (sub.isEmpty()) return true;
		var subOpt = BaseArtifact.getStats(sub);
		return subOpt.isEmpty();
	}

	private boolean isAllowedAsSubArtifact(int index, ItemStack e) {
		ItemStack main = getMainItem();
		if (main.isEmpty()) return false;
		BaseArtifact mainItem = (BaseArtifact) getMainItem().getItem();
		if (!(e.getItem() instanceof BaseArtifact eItem)) return false;
		if (eItem.set.get() != mainItem.set.get()) return false;
		int rank = mainItem.rank;
		if (index >= rank - 1) return false;
		var mainOpt = BaseArtifact.getStats(main);
		if (mainOpt.isEmpty()) return false;
		if (mainOpt.get().level < ArtifactUpgradeManager.getMaxLevel(rank)) return false;
		var mainType = mainOpt.get().main_stat.type;
		var eOpt = BaseArtifact.getStats(e);
		if (eOpt.isEmpty()) return false;
		if (eOpt.get().level < ArtifactUpgradeManager.getMaxLevel(rank)) return false;
		var eType = eOpt.get().main_stat.type;
		if (mainType == eType) return false;
		for (int i = 0; i < rank - 1; i++) {
			if (i == index) continue;
			ItemStack other = ShapeSlots.ARTIFACT_SUB.get(this, i).getItem();
			if (other.isEmpty()) continue;
			var subOpt = BaseArtifact.getStats(other);
			assert subOpt.isPresent();
			var subType = subOpt.get().main_stat.type;
			if (subType == eType) return false;
		}
		return true;
	}

	@Override
	protected void securedServerSlotChange(Container cont) {
		ShapeSlots.BOOST_MAIN.get(this).updateEject(player);
		for (int i = 0; i < 4; i++) {
			ShapeSlots.ARTIFACT_SUB.get(this, i).updateEject(player);
		}
		for (int i = 0; i < 4; i++) {
			ShapeSlots.STAT_SUB.get(this, i).updateEject(player);
		}
		for (int i = 0; i < 4; i++) {
			ShapeSlots.BOOST_SUB.get(this, i).updateEject(player);
		}
		boolean outputChanged = ShapeSlots.OUTPUT.get(this).clearDirty();
		if (!outputChanged && !getMainItem().isEmpty()) {
			BaseArtifact artifact = (BaseArtifact) getMainItem().getItem();
			int rank = artifact.rank;

			boolean pass = !ShapeSlots.BOOST_MAIN.get(this).getItem().isEmpty();
			for (int i = 0; i < rank - 1; i++) {
				pass &= !ShapeSlots.ARTIFACT_SUB.get(this, i).getItem().isEmpty();
				pass &= !ShapeSlots.STAT_SUB.get(this, i).getItem().isEmpty();
				pass &= !ShapeSlots.BOOST_SUB.get(this, i).getItem().isEmpty();
			}
			if (pass) {
				ItemStack result = new ItemStack(artifact, 1);
				var r = player.getRandom();
				ArtifactStats stat = new ArtifactStats(artifact.slot.get(), rank);
				var mainOpt = BaseArtifact.getStats(getMainItem());
				assert mainOpt.isPresent();
				var mainStat = mainOpt.get().main_stat.type;
				stat.add(mainStat, mainStat.get().getInitialValue(rank, r, true));
				for (int i = 0; i < rank - 1; i++) {
					var subOpt = BaseArtifact.getStats(ShapeSlots.ARTIFACT_SUB.get(this, i).getItem());
					assert subOpt.isPresent();
					var subStat = subOpt.get().main_stat.type;
					stat.add(subStat, subStat.get().getInitialValue(rank, r, true));
				}
				TagCodec.toTag(ItemCompoundTag.of(result).getSubTag(BaseArtifact.KEY).getOrCreate(), stat);
				ShapeSlots.OUTPUT.get(this).set(result);
			} else {
				ShapeSlots.OUTPUT.get(this).set(ItemStack.EMPTY);
			}
			ShapeSlots.OUTPUT.get(this).clearDirty();
		}
		if (outputChanged && !getMainItem().isEmpty()) {
			BaseArtifact artifact = (BaseArtifact) getMainItem().getItem();
			int rank = artifact.rank;
			ShapeSlots.ARTIFACT_MAIN.get(this).remove(1);
			ShapeSlots.BOOST_MAIN.get(this).remove(1);
			for (int i = 0; i < rank - 1; i++) {
				ShapeSlots.ARTIFACT_SUB.get(this, i).remove(1);
				ShapeSlots.STAT_SUB.get(this, i).remove(1);
				ShapeSlots.BOOST_SUB.get(this, i).remove(1);
			}
		}
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		return false;
	}

	@Override
	protected boolean shouldClear(Container container, int slot) {
		return slot != 0;
	}

}
