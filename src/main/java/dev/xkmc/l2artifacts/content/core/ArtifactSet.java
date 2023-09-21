package dev.xkmc.l2artifacts.content.core;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2artifacts.content.client.tab.DarkTextColorRanks;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.events.ArtifactEffectEvents;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtifactSet extends NamedEntry<ArtifactSet> implements IArtifactFeature.ItemIcon {

	public record SetContext(int count, int[] ranks, int current_index) {

	}

	private static int[] remapRanks(int[] rank) {
		for (int i = rank.length - 2; i >= 0; i--) {
			rank[i] += rank[i + 1];
		}
		// now rank[i] means how many items are equal or above this rank
		// ranks[i] means the maximum rank for count of i
		int[] ranks = new int[6];
		for (int i = 0; i <= 5; i++) { // count
			for (int j = 0; j < rank.length; j++) { // rank
				if (rank[j] >= i) {
					ranks[i] = Math.max(ranks[i], j);
				}
			}
		}
		return ranks;
	}

	private MutableComponent getCountDesc(int count) {
		int max = L2Artifacts.REGISTRATE.SET_MAP.get(getRegistryName()).items.length;
		return LangData.getTranslate("set." + count, max);
	}

	public ArtifactSet() {
		super(ArtifactTypeRegistry.SET);
	}

	public Optional<SetContext> getCountAndIndex(@Nullable SlotContext context) {
		LivingEntity e = context == null ? Proxy.getPlayer() : context.entity();
		if (e != null) {
			List<SlotResult> list = CuriosApi.getCuriosHelper().findCurios(e, stack -> stack.getItem() instanceof BaseArtifact artifact && artifact.set.get() == this);
			int[] rank = new int[ArtifactConfig.COMMON.maxRank.get() + 1];
			int index = -1;
			int count = 0;
			for (SlotResult result : list) {
				if (result.stack().getItem() instanceof BaseArtifact artifact && artifact.set.get() == this) {
					rank[artifact.rank]++;
					if (context != null && context.identifier().equals(result.slotContext().identifier()) && context.index() == result.slotContext().index())
						index = count;
					count++;
				}
			}

			return Optional.of(new SetContext(list.size(), remapRanks(rank), index));
		}
		return Optional.empty();
	}

	public SetContext getSetCount(LivingEntity e) {
		List<SlotResult> list = CuriosApi.getCuriosHelper().findCurios(e, stack -> stack.getItem() instanceof BaseArtifact artifact && artifact.set.get() == this);
		int[] rank = new int[ArtifactConfig.COMMON.maxRank.get() + 1];
		for (SlotResult result : list) {
			if (result.stack().getItem() instanceof BaseArtifact artifact && artifact.set.get() == this) {
				rank[artifact.rank]++;
			}
		}
		return new SetContext(list.size(), remapRanks(rank), -1);
	}

	public void update(SlotContext context) {
		LivingEntity e = context.entity();
		Optional<SetContext> result = getCountAndIndex(context);
		if (result.isPresent()) {
			ArtifactSetConfig config = ArtifactSetConfig.getInstance();
			ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
			for (ArtifactSetConfig.Entry ent : list) {
				ent.effect.update(e, ent, result.get().ranks()[ent.count], result.get().count() >= ent.count);
			}
		}
	}

	public void tick(SlotContext context) {
		LivingEntity e = context.entity();
		if (e instanceof Player player) {
			Optional<SetContext> result = getCountAndIndex(context);
			if (result.isPresent() && result.get().current_index() == 0) {
				ArtifactSetConfig config = ArtifactSetConfig.getInstance();
				ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
				for (ArtifactSetConfig.Entry ent : list) {
					ent.effect.tick(player, ent, result.get().ranks()[ent.count], result.get().count() >= ent.count);
				}
			}
		}
	}

	public <T> void propagateEvent(SlotContext context, T event, ArtifactEffectEvents.EventConsumer<T> cons) {
		LivingEntity e = context.entity();
		if (e instanceof Player player) {
			Optional<SetContext> result = getCountAndIndex(context);
			if (result.isPresent() && result.get().current_index() == 0) {
				ArtifactSetConfig config = ArtifactSetConfig.getInstance();
				ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
				for (ArtifactSetConfig.Entry ent : list) {
					if (result.get().count() >= ent.count) {
						cons.apply(ent.effect, player, ent, result.get().ranks()[ent.count], event);
					}
				}
			}
		}
	}

	public <T> boolean propagateEvent(SlotContext context, T event, ArtifactEffectEvents.EventPredicate<T> cons) {
		LivingEntity e = context.entity();
		boolean ans = false;
		if (e instanceof Player player) {
			Optional<SetContext> result = getCountAndIndex(context);
			if (result.isPresent() && result.get().current_index() == 0) {
				ArtifactSetConfig config = ArtifactSetConfig.getInstance();
				ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
				for (ArtifactSetConfig.Entry ent : list) {
					if (result.get().count() >= ent.count) {
						ans |= cons.apply(ent.effect, player, ent, result.get().ranks()[ent.count], event);
					}
				}
			}
		}
		return ans;
	}

	public List<MutableComponent> getAllDescs(ItemStack stack, boolean show) {
		List<MutableComponent> ans = new ArrayList<>();
		BaseArtifact artifact = (BaseArtifact) stack.getItem();
		if (Proxy.getPlayer() != null) {
			SetContext ctx = getSetCount(Proxy.getPlayer());
			ans.add(LangData.SET.get(Component.translatable(getDescriptionId()).withStyle(ChatFormatting.YELLOW)));
			if (show) {
				ArtifactSetConfig config = ArtifactSetConfig.getInstance();
				ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
				for (ArtifactSetConfig.Entry ent : list) {
					ChatFormatting color_count = ctx.count() < ent.count ?
							ChatFormatting.GRAY : ChatFormatting.GREEN;
					ChatFormatting color_title = ctx.count() < ent.count || ctx.ranks()[ent.count] < artifact.rank ?
							ChatFormatting.GRAY : ChatFormatting.GREEN;
					ChatFormatting color_desc = ctx.count() < ent.count || ctx.ranks()[ent.count] < artifact.rank ?
							ChatFormatting.DARK_GRAY : ChatFormatting.DARK_GREEN;
					ans.add(getCountDesc(ent.count).withStyle(color_count).append(ent.effect.getDesc().withStyle(color_title)));
					List<MutableComponent> desc = ent.effect.getDetailedDescription(artifact.rank);
					for (MutableComponent comp : desc) {
						ans.add(comp.withStyle(color_desc));
					}
				}
			}
		}
		return ans;
	}

	public List<Pair<List<Component>, List<Component>>> addComponents(SetContext ctx) {
		List<Pair<List<Component>, List<Component>>> ans = new ArrayList<>();
		ans.add(Pair.of(List.of(LangData.ALL_SET_EFFECTS.get(getDesc(), ctx.count())), List.of()));
		ArtifactSetConfig config = ArtifactSetConfig.getInstance();
		ArrayList<ArtifactSetConfig.Entry> list = config.map.get(this);
		for (ArtifactSetConfig.Entry ent : list) {
			if (ctx.count() >= ent.count) {
				int rank = ctx.ranks[ent.count];
				List<Component> a = List.of(getCountDesc(ent.count), ent.effect.getDesc()
						.withStyle(DarkTextColorRanks.getDark(rank)));
				List<Component> b = new ArrayList<>();
				b.add(getCountDesc(ent.count).append(ent.effect.getDesc()
						.withStyle(DarkTextColorRanks.getLight(rank))));
				b.addAll(ent.effect.getDetailedDescription(rank).stream()
						.map(comp -> (Component) comp.withStyle(DarkTextColorRanks.getDark(rank))).toList());
				ans.add(Pair.of(a, b));
			}
		}
		return ans;
	}

	@Override
	public Item getItemIcon() {
		var arr = L2Artifacts.REGISTRATE.SET_MAP.get(getRegistryName()).items;
		return arr[0][arr[0].length - 1].get();
	}

	@Nullable
	@Override
	public NonNullList<ItemStack> getTooltipItems() {
		var arr = L2Artifacts.REGISTRATE.SET_MAP.get(getRegistryName()).items;
		NonNullList<ItemStack> ans = NonNullList.create();
		for (var ar : arr) {
			ans.add(ar[ar.length - 1].asStack());
		}
		return ans;
	}
}
