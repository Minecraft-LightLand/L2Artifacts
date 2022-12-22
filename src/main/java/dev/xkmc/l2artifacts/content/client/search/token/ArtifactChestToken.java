package dev.xkmc.l2artifacts.content.client.search.token;

import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.serial.SerialClass;
import dev.xkmc.l2library.util.code.GenericItemStack;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@SerialClass
public class ArtifactChestToken implements IArtifactFilter {

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
	private List<ItemStack> cahce = null;

	public ArtifactChestToken(List<ItemStack> list) {
		this.list = list;
		rank = addFilter(e -> new ArtifactFilter<>(e, RankToken.ALL_RANKS,
				(item, rank) -> item.item().rank == rank.rank()));
		set = addFilter(e -> new ArtifactFilter<>(e, ArtifactTypeRegistry.SET.get().getValues(),
				(item, set) -> item.item().set.get() == set));
		slot = addFilter(e -> new ArtifactFilter<>(set, ArtifactTypeRegistry.SLOT.get().getValues(),
				(item, slot) -> item.item().slot.get() == slot));
		stat = addFilter(e -> new ArtifactFilter<>(e, ArtifactTypeRegistry.STAT_TYPE.get().getValues(), (item, type) ->
				BaseArtifact.getStats(item.stack()).map(x -> x.map.containsKey(type)).orElse(false)));
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

	public List<ItemStack> getFiltered() {
		if (cahce != null) return cahce;
		cahce = stat.getAvailableArtifacts().map(GenericItemStack::stack).toList();
		return cahce;
	}

}
