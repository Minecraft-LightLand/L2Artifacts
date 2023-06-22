package dev.xkmc.l2artifacts.events;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.effects.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

@Mod.EventBusSubscriber(modid = L2Artifacts.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

	public static <T> boolean postEvent(LivingEntity entity, T event, EventPredicate<T> cons) {
		if (!(entity instanceof Player))
			return false;
		List<SlotResult> list = CuriosApi.getCuriosHelper().findCurios(entity, stack -> stack.getItem() instanceof BaseArtifact);
		boolean ans = false;
		for (SlotResult result : list) {
			ItemStack stack = result.stack();
			BaseArtifact base = (BaseArtifact) stack.getItem();
			ans |= base.set.get().propagateEvent(result.slotContext(), event, cons);
		}
		return ans;
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

	public interface EventConsumer<T> {

		void apply(SetEffect set, Player player, ArtifactSetConfig.Entry ent, int rank, T event);

	}

	public interface EventPredicate<T> {

		boolean apply(SetEffect set, Player player, ArtifactSetConfig.Entry ent, int rank, T event);

	}

}
