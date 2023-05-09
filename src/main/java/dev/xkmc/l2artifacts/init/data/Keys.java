package dev.xkmc.l2artifacts.init.data;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum Keys {
	UP("key.l2artifacts.up", GLFW.GLFW_KEY_UP),
	DOWN("key.l2artifacts.down", GLFW.GLFW_KEY_DOWN),
	SWAP("key.l2artifacts.swap", GLFW.GLFW_KEY_R);

	public final String id;
	public final int key;
	public final KeyMapping map;

	Keys(String id, int key) {
		this.id = id;
		this.key = key;
		map = new KeyMapping(id, key, "key.categories.l2artifacts");
	}

}
