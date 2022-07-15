package dev.xkmc.l2artifacts.content.misc;

import dev.xkmc.l2artifacts.content.upgrades.ArtifactUpgradeManager;
import dev.xkmc.l2artifacts.init.data.LangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExpItem extends Item {

	public final int rank;

	public ExpItem(Properties properties, int rank) {
		super(properties);
		this.rank = rank;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.EXP_CONVERSION.get(ArtifactUpgradeManager.getExpForConversion(rank, null)));
		super.appendHoverText(stack, level, list, flag);
	}
}
