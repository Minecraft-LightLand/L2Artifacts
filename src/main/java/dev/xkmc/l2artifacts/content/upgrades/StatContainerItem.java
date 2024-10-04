package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2core.util.ServerProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StatContainerItem extends UpgradeEnhanceItem {

	private static final String KEY = "stat";

	public static ItemStack setStat(ItemStack item, Holder<StatType> type) {
		return ArtifactItems.STAT.set(item, type.toString());
	}

	public static Optional<Holder<StatType>> getType(@Nullable RegistryAccess access, ItemStack item) {
		if (access == null || item.isEmpty()) return Optional.empty();
		var str = ArtifactItems.STAT.get(item);
		if (str == null) return Optional.empty();
		var id = ResourceLocation.tryParse(str);
		if (id == null) return Optional.empty();
		var val = ArtifactTypeRegistry.STAT_TYPE.get(access, id);
		return Optional.ofNullable(val);
	}

	public StatContainerItem(Properties props, int rank) {
		super(props, rank);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		if (ctx.registries() == null) return;
		var access = ServerProxy.getRegistryAccess();
		if (access == null) return;
		getType(access, stack).ifPresentOrElse(e -> {
			list.add(ArtifactLang.STAT_INFO.get(e.value().getDesc().withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.DARK_GREEN));
			list.add(ArtifactLang.STAT_USE_INFO.get().withStyle(ChatFormatting.GRAY));
		}, () -> list.add(ArtifactLang.STAT_CAPTURE_INFO.get().withStyle(ChatFormatting.GRAY)));
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return getType(ServerProxy.getRegistryAccess(), pStack).isPresent();
	}
}
