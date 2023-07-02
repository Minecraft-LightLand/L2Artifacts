package dev.xkmc.l2artifacts.content.search.upgrade;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.common.IntDataSlot;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.L2Artifacts;
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

public class UpgradeMenu extends BaseContainerMenu<UpgradeMenu> implements IFilterMenu {

	private static final SpriteManager MANAGER = new SpriteManager(L2Artifacts.MODID, "upgrade");

	public static UpgradeMenu fromNetwork(MenuType<UpgradeMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		int i = buf.readInt();
		InteractionHand hand = i == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		return new UpgradeMenu(wid, plInv, ArtifactChestToken.of(plInv.player, hand));
	}

	public final ArtifactChestToken token;
	public final Player player;

	public final IntDataSlot experience;
	public final IntDataSlot exp_cost;

	public UpgradeMenu(int wid, Inventory plInv, ArtifactChestToken token) {
		super(ArtifactMenuRegistry.MT_UPGRADE.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(1, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("input", e -> e.getItem() instanceof BaseArtifact);
		this.experience = new IntDataSlot(this);
		this.exp_cost = new IntDataSlot(this);
		experience.set(token.exp);
	}

	@Override
	public void slotsChanged(Container cont) {
		if (player.level.isClientSide) return;
		ItemStack stack = cont.getItem(0);
		int ec = 0;
		int pc = 0;
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
				}
			}
		}
		exp_cost.set(ec);
		super.slotsChanged(cont);
	}

	@Override
	public boolean clickMenuButton(Player player, int data) {
		if (data == 0) {
			boolean canUpgrade = exp_cost.get() > 0 && exp_cost.get() <= experience.get();
			if (player.level.isClientSide) {
				return canUpgrade;
			}
			if (!canUpgrade)
				return false;
			ItemStack stack = container.getItem(0);
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
