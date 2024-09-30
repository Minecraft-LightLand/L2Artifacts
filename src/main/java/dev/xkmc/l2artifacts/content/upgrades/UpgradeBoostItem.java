package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.init.data.ArtifactLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeBoostItem extends UpgradeEnhanceItem {

	public final Upgrade.Type type;

	public UpgradeBoostItem(Properties props, int rank, Upgrade.Type type) {
		super(props, rank);
		this.type = type;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add((type == Upgrade.Type.BOOST_MAIN_STAT ? ArtifactLang.BOOST_MAIN : ArtifactLang.BOOST_SUB).get().withStyle(ChatFormatting.GRAY));
	}

}
