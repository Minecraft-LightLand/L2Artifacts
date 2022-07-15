package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class Upgrade {

	@SerialClass.SerialField
	public int main, sub;
	@SerialClass.SerialField
	public ArrayList<ArtifactStatType> stats = new ArrayList<>();

	public void addTooltips(List<Component> list) {
		if (main > 0) {
			list.add(LangData.UPGRADE_MAIN.get(main));
		}
		if (sub > 0) {
			list.add(LangData.UPGRADE_SUB.get(sub));
		}
		for (ArtifactStatType stat : stats) {
			list.add(LangData.UPGRADE_STAT.get(stat));
		}
	}

	public boolean removeMain() {
		if (main > 0) {
			main--;
			return true;
		}
		return false;
	}

	public boolean removeSub() {
		if (sub > 0) {
			sub--;
			return true;
		}
		return false;
	}

	public void add(Type type) {
		if (type == Type.BOOST_MAIN_STAT) main++;
		if (type == Type.BOOST_SUB_STAT) sub++;
	}

	public enum Type {
		BOOST_MAIN_STAT, BOOST_SUB_STAT, SET_SUB_STAT
	}

}
