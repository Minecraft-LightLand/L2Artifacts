package dev.xkmc.l2artifacts.content.search.filter;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.content.config.StatTypeHolder;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.search.token.FilledTokenData;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.util.GenericItemStack;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@SerialClass
public class ArtifactFilterData implements IArtifactFilter {

	@SerialField
	private final ArtifactFilter<RankToken> rank;

	@SerialField
	private final ArtifactFilter<ArtifactSet> set;

	@SerialField
	private final ArtifactFilter<ArtifactSlot> slot;

	@SerialField
	private final ArtifactFilter<StatTypeHolder> stat;

	public final List<ArtifactFilter<?>> filters = new ArrayList<>();

	public ArtifactFilterData() {
		rank = addFilter(e -> new RankFilter(e, ArtifactLang.FILTER_RANK));
		set = addFilter(e -> new SimpleArtifactFilter<>(e, ArtifactLang.FILTER_SET,
				ArtifactTypeRegistry.SET.get(), i -> i.set.get()));
		slot = addFilter(e -> new SimpleArtifactFilter<>(e, ArtifactLang.FILTER_SLOT,
				ArtifactTypeRegistry.SLOT.get(), i -> i.slot.get()));
		stat = addFilter(e -> new AttributeFilter(e, ArtifactLang.FILTER_STAT, StatType.getValues()));
	}

	private <T extends IArtifactFeature> ArtifactFilter<T> addFilter(Function<IArtifactFilter, ArtifactFilter<T>> gen) {
		var ans = gen.apply(filters.isEmpty() ? this : filters.getLast());
		filters.add(ans);
		return ans;
	}

	@Override
	public Comparator<GenericItemStack<BaseArtifact>> getComparator() {
		var list = new ArrayList<>(filters);
		list.sort(Comparator.comparingInt(e -> e.sort_priority));
		Comparator<GenericItemStack<BaseArtifact>> ans = null;
		assert !list.isEmpty();
		for (var e : list) {
			if (ans == null) {
				ans = e.getComparator();
			} else {
				ans = ans.thenComparing(e.getComparator());
			}
		}
		return ans;
	}

	@Override
	public Stream<GenericItemStack<BaseArtifact>> getFilteredImpl(FilledTokenData data) {
		return data.getAll().stream().map(e -> new GenericItemStack<>((BaseArtifact) e.getItem(), e));
	}

	@Override
	public void update(FilledTokenData data) {
		data.clearCache();
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

}
