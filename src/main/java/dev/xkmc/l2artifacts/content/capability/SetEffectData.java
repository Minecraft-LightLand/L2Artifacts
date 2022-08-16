package dev.xkmc.l2artifacts.content.capability;

import dev.xkmc.l2library.serial.SerialClass;

@SerialClass
public class SetEffectData {

	@SerialClass.SerialField
	public int life, rank;

	@SerialClass.SerialField
	public boolean hasRemove;

	private Runnable onRemove;

	public boolean tick() {
		if (life > 0)
			life--;
		if (life == 0) {
			if (!remove()) {
				life = 1;
			}
		}
		return life <= 0;
	}

	public void update(int time, int rank) {
		life = time;
		this.rank = rank;
	}

	public void setRemoveCallback(Runnable run) {
		hasRemove = true;
		onRemove = run;
	}

	protected boolean remove() {
		if (!hasRemove) return true;
		if (onRemove == null) return false;
		onRemove.run();
		return true;
	}
}
