package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class ArtifactEffectEvents {

	public static <T> void postEvent(LivingEntity entity, T event, EventConsumer<T> cons) {
		if (!(entity instanceof Player))
			return;
		List<SlotResult> list = CuriosApi.getCuriosHelper().findCurios(entity, stack -> stack.getItem() instanceof BaseArtifact);
		for (SlotResult result : list) {
			ItemStack stack = result.stack();
			BaseArtifact base = (BaseArtifact) stack.getItem();
			base.set.get().propagateEvent(result.slotContext(), event, cons);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onAttackEvent(CriticalHitEvent event) {
		postEvent(event.getEntity(), event, SetEffect::playerAttackModifyEvent);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onHurtEventPost(LivingHurtEvent event) {
		postEvent(event.getEntity(), event, SetEffect::playerHurtEvent);
	}

	@SubscribeEvent
	public static void onKillEvent(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof Player player)
			postEvent(player, event, SetEffect::playerKillOpponentEvent);
	}

}
