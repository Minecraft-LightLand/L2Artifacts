package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.ArtifactStatType;
import dev.xkmc.l2artifacts.content.core.ArtifactStatTypeHolder;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class Upgrade {

	@SerialClass.SerialField
	public int main, sub;
	@SerialClass.SerialField
	public ArrayList<ArtifactStatTypeHolder> stats = new ArrayList<>();

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
