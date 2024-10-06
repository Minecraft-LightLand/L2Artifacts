package dev.xkmc.l2artifacts.compat.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2backpack.content.quickswap.entry.*;
import dev.xkmc.l2backpack.content.quickswap.type.ISetSwapAction;
import dev.xkmc.l2backpack.content.quickswap.type.ISideInfoRenderer;
import dev.xkmc.l2backpack.content.quickswap.type.ISingleSwapAction;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2backpack.init.data.LBConfig;
import dev.xkmc.l2itemselector.overlay.OverlayUtil;
import dev.xkmc.l2itemselector.overlay.SelectionSideBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;

public class ArtifactSwapType extends MatcherSwapType implements ISideInfoRenderer, ISingleSwapAction, ISetSwapAction {

	public static ArtifactSlot getSlot(ItemStack stack) {
		return ((BaseArtifact) stack.getItem()).slot.get();
	}

	public static ArtifactSlot getSlot(int index) {
		return ArtifactSlotCuriosType.values()[index].slot;
	}

	public ArtifactSwapType(String name) {
		super(name, true);
	}

	@Override
	public boolean match(ItemStack stack) {
		return stack.is(ArtifactItems.SWAP);
	}

	public boolean activePopup() {
		return LBConfig.CLIENT.popupArmorOnSwitch.get();
	}

	public ItemStack getSignatureItem(Player player) {
		return ItemStack.EMPTY;
	}

	public void swapSingle(Player player, ISingleSwapHandler handler) {
		ItemStack stack = handler.getStack();
		if (!stack.isEmpty()) {
			var slot = getSlot(stack);
			var curios = SlotHandler.of(player, slot);
			if (curios == null) return;
			handler.replace(curios.getStack());
			curios.setStack(stack);
		}
	}

	public void swapSet(Player player, ISetSwapHandler handler) {
		for (int i = 0; i < 5; ++i) {
			if (!handler.isLocked(i)) {
				ItemStack stack = handler.getStack(i);
				var slot = getSlot(i);
				var curios = CuriosApi.getCuriosInventory(player)
						.flatMap(e -> e.getStacksHandler(slot.getCurioIdentifier()));
				if (curios.isEmpty() || curios.get().getSlots() == 0)
					continue;
				handler.replace(i, curios.get().getStacks().getStackInSlot(0));
				curios.get().getStacks().setStackInSlot(0, stack);
			}
		}

	}

	public boolean isAvailable(Player player, ISwapEntry<?> token) {
		if (token instanceof SingleSwapEntry single) {
			ItemStack stack = single.stack();
			return !stack.isEmpty();
		} else if (token instanceof SetSwapEntry) {
			for (int i = 0; i < 5; ++i) {
				if (this.isAvailable(player, token, i)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public boolean isAvailable(Player player, ISwapEntry<?> token, int index) {
		var e = SlotHandler.of(player, getSlot(index));
		if (e == null) return false;
		ItemStack old = e.getStack();
		ItemStack cur = token.asList().get(index);
		return !old.isEmpty() || !cur.isEmpty();
	}

	public void renderSide(SelectionSideBar.Context ctx, int x, int y, Player player, ISwapEntry<?> token) {
		SlotHandler e;
		if (token instanceof SingleSwapEntry single) {
			ItemStack hover = single.stack();
			e = SlotHandler.of(player, getSlot(hover));
			for (int i = 0; i < 5; ++i) {
				var slot = SlotHandler.of(player, getSlot(i));
				ItemStack stack = slot == null ? ItemStack.EMPTY : slot.getStack();
				renderArmorSlot(ctx.g(), x, y, 64, e != null && e == slot, e == null);
				ctx.renderItem(stack, x, y);
				y += 18;
			}
		}

		if (token instanceof SetSwapEntry set) {
			for (int i = 0; i < 5; ++i) {
				e = SlotHandler.of(player, getSlot(i));
				ItemStack old = e == null ? ItemStack.EMPTY : e.getStack();
				ItemStack cur = set.asList().get(i);
				boolean avail = e != null && (!old.isEmpty() || !cur.isEmpty());
				renderArmorSlot(ctx.g(), x, y, 64, !set.isLocked(i) && (!old.isEmpty() || !cur.isEmpty()), !avail);
				ctx.renderItem(old, x, y);
				y += 18;
			}
		}

	}

	private static void renderArmorSlot(GuiGraphics g, int x, int y, int a, boolean target, boolean invalid) {
		OverlayUtil.fillRect(g, x, y, 16, 16, color(255, 255, 255, a));
		if (target) {
			if (invalid) {
				OverlayUtil.drawRect(g, x, y, 16, 16, color(220, 70, 70, 255));
			} else {
				OverlayUtil.drawRect(g, x, y, 16, 16, color(70, 150, 185, 255));
			}
		}

	}

	private record SlotHandler(IDynamicStackHandler stacks) {

		@Nullable
		public static SlotHandler of(Player player, ArtifactSlot slot) {
			var handler = CuriosApi.getCuriosInventory(player)
					.flatMap(e -> e.getStacksHandler(slot.getCurioIdentifier()));
			if (handler.isEmpty() || handler.get().getSlots() == 0) return null;
			return new SlotHandler(handler.get().getStacks());
		}


		public ItemStack getStack() {
			return stacks.getStackInSlot(0);
		}

		public void setStack(ItemStack stack) {
			stacks.setStackInSlot(0, stack);
		}
	}

}
