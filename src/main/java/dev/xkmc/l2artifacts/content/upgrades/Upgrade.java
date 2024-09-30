package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.config.StatType;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;

import java.util.ArrayList;

@SerialClass
public class Upgrade {

	@SerialField
	public int main, sub;
	@SerialField
	public ArrayList<Holder<StatType>> stats = new ArrayList<>();

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
