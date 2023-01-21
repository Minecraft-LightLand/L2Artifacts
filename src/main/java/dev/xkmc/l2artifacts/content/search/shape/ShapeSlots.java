package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2library.base.menu.PredSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public enum ShapeSlots {
	OUTPUT, ARTIFACT_MAIN, BOOST_MAIN, ARTIFACT_SUB, STAT_SUB, BOOST_SUB;

	public String slot() {
		return name().toLowerCase(Locale.ROOT);
	}

	public PredSlot get(ShapeMenu menu) {
		return menu.getAsPredSlot(this);
	}

	public PredSlot get(ShapeMenu menu, int i) {
		return menu.getAsPredSlot(this, i);
	}

}
