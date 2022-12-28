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
		ans.exp = ArtifactChestItem.getExp(stack);
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

	public int exp = 0;

	@Nullable
	private List<GenericItemStack<BaseArtifact>> cahce = null;

	private ArtifactChestToken(ItemStack stack, List<ItemStack> list, InteractionHand hand) {
		this.list = list;
		this.stack = stack;
		this.hand = hand;
		rank = addFilter(e -> new RankFilter(e, LangData.FILTER_RANK));
		slot = addFilter(e -> new SimpleArtifactFilter<>(e, LangData.FILTER_SLOT,
				ArtifactTypeRegistry.SLOT.get().getValues(), i -> i.slot.get()));
		set = addFilter(e -> new SimpleArtifactFilter<>(e, LangData.FILTER_SET,
				ArtifactTypeRegistry.SET.get().getValues(), i -> i.set.get()));
		stat = addFilter(e -> new AttributeFilter(e, LangData.FILTER_STAT, ArtifactTypeRegistry.STAT_TYPE.get().getValues()));
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

	public void prioritize(int ind) {
		filters.get(ind).sort_priority = 0;
		List<ArtifactFilter<?>> list = new ArrayList<>(filters);
		list.sort(Comparator.comparingInt(e -> e.sort_priority));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).sort_priority = i + 1;
		}
	}

	@Override
	public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
		var list = new ArrayList<>(filters);
		list.sort(Comparator.comparingInt(e -> e.sort_priority));
		Comparator<GenericItemStack<BaseArtifact>> ans = null;
		assert list.size() > 0;
		for (var e : list) {
			if (ans == null) {
				ans = e.getComparator();
			} else {
				ans = ans.thenComparing(e.getComparator());
			}
		}
		return ans;
	}

	@Deprecated
	@Override
	public Stream<GenericItemStack<BaseArtifact>> getAvailableArtifacts() {
		return list.stream().map(e -> new GenericItemStack<>((BaseArtifact) e.getItem(), e));
	}

	public List<GenericItemStack<BaseArtifact>> getFiltered() {
		if (cahce != null) return cahce;
		cahce = stat.getAvailableArtifacts().sorted(getComparator()).toList();
		return cahce;
	}

	public void save() {
		ArtifactChestItem.setContent(stack, list);
	}

}
