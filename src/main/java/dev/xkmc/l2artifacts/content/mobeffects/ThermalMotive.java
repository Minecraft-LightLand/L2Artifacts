package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ThermalMotive extends InherentEffect {

	public ThermalMotive(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return List.of();
	}

}
