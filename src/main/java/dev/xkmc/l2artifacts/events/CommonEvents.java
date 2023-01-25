package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.core.ArtifactCurioCap;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.event.CuriosEventHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class CommonEvents {

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		ItemStack stack = event.getObject();
		if (!(stack.getItem() instanceof BaseArtifact))
			return;
		final LazyOptional<ICurio> stats = LazyOptional.of(() -> new ArtifactCurioCap(stack));
		event.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
			@Nonnull
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, LazyOptional.of(() -> new ArtifactCurioCap(stack)));
			}
		});
		Objects.requireNonNull(stats);
		event.addListener(stats::invalidate);
	}

	@SubscribeEvent
	public static void revive(PlayerEvent.PlayerRespawnEvent event){
		new CuriosEventHandler().tick(new LivingEvent.LivingTickEvent(event.getEntity()));
		event.getEntity().setHealth(event.getEntity().getMaxHealth());
	}

}
