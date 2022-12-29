package dev.xkmc.l2artifacts.content.search.augment;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
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

	public AugmentMenu(int wid, Inventory plInv, ArtifactChestToken token) {//TODO
		super(ArtifactMenuRegistry.MT_AUGMENT.get(), wid, plInv, MANAGER, e -> new BaseContainer<>(1, e), true);
		this.token = token;
		this.player = plInv.player;
		addSlot("input", e -> e.getItem() instanceof BaseArtifact);
		this.experience = addDataSlot(DataSlot.standalone());
		this.exp_cost = addDataSlot(DataSlot.standalone());
		this.player_cost = addDataSlot(DataSlot.standalone());
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
		player_cost.set(pc);
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
