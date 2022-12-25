package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.ArtifactChestItem;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.serial.codec.TagCodec;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@SerialClass
public class ArtifactChestToken implements IArtifactFilter {

	public static ArtifactChestToken of(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		List<ItemStack> list = ArtifactChestItem.getContent(stack);
		ArtifactChestToken ans = new ArtifactChestToken(stack, list, hand);
		TagCodec.fromTag(ArtifactChestItem.getFilter(stack), ArtifactChestToken.class, ans, e -> true);
		return ans;
	}

	public final ItemStack stack;
	public final InteractionHand hand;
	public final List<ItemStack> list;
	public final List<ArtifactFilter<?>> filters = new ArrayList<>();

	@SerialClass.SerialField
	public final ArtifactFilter<RankToken> rank;

	@SerialClass.SerialField
	public final ArtifactFilter<ArtifactSet> set;

	@SerialClass.SerialField
	public final ArtifactFilter<ArtifactSlot> slot;

	@SerialClass.SerialField
	public final ArtifactFilter<ArtifactStatType> stat;

	@Nullable
	private List<GenericItemStack<BaseArtifact>> cahce = null;

	private Comparator<GenericItemStack<BaseArtifact>> comparator;

	private ArtifactChestToken(ItemStack stack, List<ItemStack> list, InteractionHand hand) {
		this.list = list;
		this.stack = stack;
		this.hand = hand;
		rank = addFilter(e -> new ArtifactFilter<>(e, LangData.FILTER_RANK, RankToken.ALL_RANKS,
				(item, rank) -> item.item().rank == rank.rank()));
		slot = addFilter(e -> new ArtifactFilter<>(e, LangData.FILTER_SLOT, ArtifactTypeRegistry.SLOT.get().getValues(),
				(item, slot) -> item.item().slot.get() == slot));
		set = addFilter(e -> new ArtifactFilter<>(e, LangData.FILTER_SET, ArtifactTypeRegistry.SET.get().getValues(),
				(item, set) -> item.item().set.get() == set));
		stat = addFilter(e -> new ArtifactFilter<>(e, LangData.FILTER_STAT, ArtifactTypeRegistry.STAT_TYPE.get().getValues(),
				(item, type) -> BaseArtifact.getStats(item.stack()).map(x -> x.map.containsKey(type)).orElse(false)));
		TagCodec.fromTag(ArtifactChestItem.getFilter(stack), ArtifactChestToken.class, this, e -> true);
	}

	private <T extends IArtifactFeature> ArtifactFilter<T> addFilter(Function<IArtifactFilter, ArtifactFilter<T>> gen) {
		var ans = gen.apply(filters.size() == 0 ? this : filters.get(filters.size() - 1));
		filters.add(ans);
		return ans;
	}

	public void update() {
		cahce = null;
		rank.clearCache();
		set.clearCache();
		slot.clearCache();
		stat.clearCache();
	}

	@Deprecated
	@Override
	public Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts() {
		return list.stream().map(e -> new GenericItemStack<>((BaseArtifact) e.getItem(), e));
	}

	public void setComparator(Comparator<GenericItemStack<BaseArtifact>> comparator) {
		this.comparator = comparator;
		cahce = null;
	}

	public List<GenericItemStack<BaseArtifact>> getFiltered() {
		if (cahce != null) return cahce;
		cahce = stat.getAvailableArtifacts().sorted(comparator).toList();
		return cahce;
	}

	public void save() {
		ArtifactChestItem.setContent(stack, list);
	}
}
