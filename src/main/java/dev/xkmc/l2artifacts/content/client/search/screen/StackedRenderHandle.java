package dev.xkmc.l2artifacts.content.client.search.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StackedRenderHandle {

	private static final int TOP_OFFSET = 3, TEXT_HEIGHT = 13;
	private static final int TEXT_X_OFFSET = 8, TEXT_Y_OFFSET = 3;
	private static final int SLOT_X_OFFSET = 7, SLOT_SIZE = 18, SPRITE_OFFSET = 176;
	private static final int TEXT_COLOR = 4210752;

	private final Screen scr;
	private final Font font;
	private final PoseStack stack;

	private int current_y = 0;
	private int current_x = 0;


	public StackedRenderHandle(Screen scr, PoseStack stack) {
		font = Minecraft.getInstance().font;
		this.stack = stack;
		this.scr = scr;
	}

	public void drawText(Component text) {
		endCell();
		int y = current_y + TEXT_Y_OFFSET;
		int x = TEXT_X_OFFSET;
		this.font.draw(stack, text, 8, 6 + 13 + 18, TEXT_COLOR);
		current_y += TEXT_HEIGHT;
	}

	public boolean addCell(boolean toggled, boolean disabled, CellCallback cb) {
		startCell();
		int index = toggled ? 1 : disabled ? 2 : 0;
		int x = SLOT_X_OFFSET + current_x * SLOT_SIZE;
		int u = SPRITE_OFFSET + index * SLOT_SIZE;
		scr.blit(stack, x, current_y, u, 0, SLOT_SIZE, SLOT_SIZE);
		boolean hover = cb.accept(x + 1, current_y + 1);
		current_x++;
		if (current_x == 9) {
			endCell();
		}
		return hover;
	}

	private void startCell() {
		if (current_x < 0) {
			current_x = 0;
		}
	}

	private void endCell() {
		if (current_x > 0) {
			current_x = -1;
			current_y += SLOT_SIZE;
		}
	}

	@FunctionalInterface
	public interface CellCallback {

		boolean accept(int x, int y);

	}

}
