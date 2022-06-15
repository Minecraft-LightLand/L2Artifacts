package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class ArtifactEffectEvents {

	public static <T extends Event> void postEvent(LivingEntity entity, T event) {
		if (!(entity instanceof Player))
			return;
		List<SlotResult> list = CuriosApi.getCuriosHelper().findCurios(entity, stack -> stack.getItem() instanceof BaseArtifact);
		for (SlotResult result : list) {
			ItemStack stack = result.stack();
			BaseArtifact base = (BaseArtifact) stack.getItem();
			base.set.get().propagateEvent(result.slotContext(), event);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onHurtEvent(LivingHurtEvent event) {
		postEvent(event.getEntityLiving(), event);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onAttackEvent(CriticalHitEvent event) {
		postEvent(event.getEntityLiving(), event);
	}

}
