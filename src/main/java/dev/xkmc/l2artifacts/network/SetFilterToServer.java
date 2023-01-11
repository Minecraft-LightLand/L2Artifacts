package dev.xkmc.l2artifacts.network;

import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.content.search.augment.AugmentMenu;
import dev.xkmc.l2artifacts.content.search.common.ArtifactChestMenuPvd;
import dev.xkmc.l2artifacts.content.search.common.IFilterMenu;
import dev.xkmc.l2artifacts.content.search.dissolve.DissolveMenu;
import dev.xkmc.l2artifacts.content.search.fitered.FilteredMenu;
import dev.xkmc.l2artifacts.content.search.recycle.RecycleMenu;
import dev.xkmc.l2artifacts.content.search.shape.ShapeMenu;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.upgrade.UpgradeMenu;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.serial.network.SerialPacketBase;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;

@SerialClass
public class SetFilterToServer extends SerialPacketBase {

	public enum Type {
		FILTER(FilteredMenu::new),
		RECYCLE(RecycleMenu::new),
		UPGRADE(UpgradeMenu::new),
		DISSOLVE(DissolveMenu::new),
		AUGMENT(AugmentMenu::new),
		SHAPE(ShapeMenu::new);

		private final ArtifactChestMenuPvd.Factory factory;

		Type(ArtifactChestMenuPvd.Factory factory) {
			this.factory = factory;
		}
	}

	@SerialClass.SerialField
	private InteractionHand hand;

	@SerialClass.SerialField
	private CompoundTag filter;

	@SerialClass.SerialField
	@Nullable
	private Type type;

	@Deprecated
	public SetFilterToServer() {

	}

	public SetFilterToServer(ArtifactChestToken token, @Nullable Type type) {
		hand = token.hand;
		filter = TagCodec.toTag(new CompoundTag(), token);
		ArtifactChestItem.setFilter(Proxy.getClientPlayer().getItemInHand(hand), filter);
		this.type = type;
	}


	@Override
	public void handle(NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null) return;
		ItemStack stack = player.getItemInHand(hand);
		if (!(stack.getItem() instanceof ArtifactChestItem))
			return;
		ArtifactChestItem.setFilter(stack, filter);
		if (type == null) {
			player.doCloseContainer();
			return;
		}
		if (player.containerMenu instanceof IFilterMenu) {
			ItemStack carried = player.containerMenu.getCarried();
			player.containerMenu.setCarried(ItemStack.EMPTY);
			new ArtifactChestMenuPvd(type.factory, player, hand, stack).open();
			player.containerMenu.setCarried(carried);
		} else {
			new ArtifactChestMenuPvd(type.factory, player, hand, stack).open();
		}
	}

}
