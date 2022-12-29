package dev.xkmc.l2artifacts.content.capability;

import dev.xkmc.l2library.serial.SerialClass;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class SetEffectData {

	@SerialClass.SerialField
	public int life, rank;

	public boolean tick(Player player) {
		if (life > 0)
			life--;
		if (life == 0) {
			remove(player);
		}
		return life <= 0;
	}

	public void update(int time, int rank) {
		life = time;
		this.rank = rank;
	}

	protected void remove(Player player) {
	}

}
