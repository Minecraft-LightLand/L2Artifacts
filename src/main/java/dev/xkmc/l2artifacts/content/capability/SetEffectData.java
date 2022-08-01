package dev.xkmc.l2artifacts.content.capability;

import dev.xkmc.l2library.serial.SerialClass;

@SerialClass
public class SetEffectData {

	@SerialClass.SerialField
	public int life, rank;

	public Runnable onRemove;

	public boolean tick() {
		if (life > 0)
			life--;
		if (life == 0) {
			remove();
		}
		return life <= 0;
	}

	public void update(int time, int rank) {
		life = time;
		this.rank = rank;
	}

	protected void remove() {
		if (onRemove != null) {
			onRemove.run();
		}
	}
}
