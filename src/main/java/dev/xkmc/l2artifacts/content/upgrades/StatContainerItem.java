package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.util.ItemCompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StatContainerItem extends UpgradeEnhanceItem {

	private static final String KEY = "stat";

	public static ItemStack setStat(ItemStack item, ArtifactStatType type) {
		item.getOrCreateTag().putString(KEY, type.getID());
		return item;
	}

	public static Optional<ArtifactStatType> getType(ItemStack item) {
		ItemCompoundTag tag = ItemCompoundTag.of(item);
		if (!tag.isPresent()) {
			return Optional.empty();
		}
		if (!tag.getOrCreate().contains(KEY, Tag.TAG_STRING)) {
			return Optional.empty();
		}
		String str = tag.getOrCreate().getString(KEY);
		if (str.length() == 0) {
			return Optional.empty();
		}
		return Optional.ofNullable(ArtifactTypeRegistry.STAT_TYPE.get().getValue(new ResourceLocation(str)));
	}

	public StatContainerItem(Properties props, int rank) {
		super(props, rank);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		getType(stack).ifPresentOrElse(e -> {
			list.add(LangData.STAT_INFO.get(e.getDesc()));
			list.add(LangData.STAT_USE_INFO.get());
		}, () -> list.add(LangData.STAT_CAPTURE_INFO.get()));
	}
}
