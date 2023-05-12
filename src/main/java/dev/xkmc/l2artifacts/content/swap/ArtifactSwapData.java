package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;

@SerialClass
public class ArtifactSwapData {

	@SerialClass
	public static class SwapSlot {

		public ArtifactSlot slot;

		@SerialClass.SerialField
		private ItemStack stack = ItemStack.EMPTY;

		@SerialClass.SerialField
		private boolean disabled = false;

		@Deprecated
		public SwapSlot() {

		}

		public SwapSlot(ArtifactSlot slot) {
			this.slot = slot;
		}

		public ItemStack getStack() {
			return stack;
		}

		public void setStack(ItemStack item) {
			stack = item;
			if (disabled && !item.isEmpty()) {
				disabled = false;
			}
		}

		public void toggle() {
			if (stack.isEmpty()) {
				disabled = !disabled;
			}
		}

		public boolean canAccept(ItemStack st) {
			return !disabled && st.getItem() instanceof BaseArtifact a && a.slot.get() == slot;
		}

		public boolean isLocked() {
			return disabled;
		}
	}

	@SerialClass.SerialField
	public final SwapSlot[] contents = new SwapSlot[45];

	@SerialClass.SerialField
	public int select = 0;

	public ArtifactSwapData() {
		int slot_ind = 0;
		for (var slot : ArtifactTypeRegistry.SLOT.get()) {
			for (int set_ind = 0; set_ind < 9; set_ind++) {
				contents[set_ind + slot_ind * 9] = new SwapSlot(slot);
			}
			slot_ind++;
		}
	}

	@SerialClass.OnInject
	public void onInject() {
		int slot_ind = 0;
		for (var slot : ArtifactTypeRegistry.SLOT.get()) {
			for (int set_ind = 0; set_ind < 9; set_ind++) {
				contents[set_ind + slot_ind * 9].slot = slot;
			}
			slot_ind++;
		}
	}

	public void swap(Player player) {
		player.getCapability(CuriosCapability.INVENTORY).resolve().ifPresent(cap -> {
			for (int slot_ind = 0; slot_ind < 5; slot_ind++) {
				SwapSlot slot = contents[slot_ind * 9 + select];
				if (slot.disabled) {
					continue;
				}
				cap.getStacksHandler(slot.slot.getCurioIdentifier()).ifPresent(h -> {
					ItemStack old = h.getStacks().getStackInSlot(0);
					ItemStack store = slot.getStack();
					if (old.isEmpty()) {
						if (!store.isEmpty()) {
							h.getStacks().setStackInSlot(0, store);
							slot.setStack(ItemStack.EMPTY);
						}
					} else if (old.getItem() instanceof BaseArtifact) {
						slot.setStack(old.copy());
						h.getStacks().setStackInSlot(0, store);
					}
				});
			}
		});
	}

}
