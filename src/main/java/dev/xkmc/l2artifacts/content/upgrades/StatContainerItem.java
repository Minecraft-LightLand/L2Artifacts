package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class StatContainerItem extends UpgradeEnhanceItem {

	private static final String KEY = "stat";

	public static ItemStack setStat(ItemStack item, ResourceLocation type) {
		item.getOrCreateTag().putString(KEY, type.toString());
		return item;
	}

	public static Optional<Holder<StatType>> getType(ItemStack item) {
		if (item.isEmpty()) return Optional.empty();
		ItemCompoundTag tag = ItemCompoundTag.of(item);
		if (!tag.isPresent()) {
			return Optional.empty();
		}
		if (!tag.getOrCreate().contains(KEY, Tag.TAG_STRING)) {
			return Optional.empty();
		}
		String str = tag.getOrCreate().getString(KEY);
		if (str.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new ResourceLocation(str));
	}

	public StatContainerItem(Properties props, int rank) {
		super(props, rank);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		if (ctx.registries() == null) return;
		var access = Proxy.getRegistryAccess();
		if (access == null) return;
		getType(stack).ifPresentOrElse(e -> {
			list.add(LangData.STAT_INFO.get(e.value().getDesc().withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.DARK_GREEN));
			list.add(LangData.STAT_USE_INFO.get().withStyle(ChatFormatting.GRAY));
		}, () -> list.add(LangData.STAT_CAPTURE_INFO.get().withStyle(ChatFormatting.GRAY)));
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return getType(pStack).isPresent();
	}
}
