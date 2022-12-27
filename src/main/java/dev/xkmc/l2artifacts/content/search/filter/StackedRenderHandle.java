package dev.xkmc.l2artifacts.content.search.filter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.menu.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class StackedRenderHandle {

	private static final int TEXT_BASE_HEIGHT = 8;
	private static final int BTN_X_OFFSET = 3;
	private static final int SLOT_X_OFFSET = 7, SLOT_SIZE = 18, SPRITE_OFFSET = 176;


	private final Screen scr;
	private final Font font;
	private final PoseStack stack;
	private final SpriteManager sm;
	private final int TEXT_Y_OFFSET;
	private final int TEXT_HEIGHT;
	private final int text_color;
	private final int text_x_offset;

	private int current_y = 3;
	private int current_x = 0;

	private final List<TextEntry> textList = new ArrayList<>();

	public StackedRenderHandle(Screen scr, PoseStack stack, SpriteManager sm) {
		this(scr, stack, sm, 3);
	}

	public StackedRenderHandle(Screen scr, PoseStack stack, SpriteManager sm, int ty) {
		this(scr, stack, 8, 4210752, sm, ty);
	}

	public StackedRenderHandle(Screen scr, PoseStack stack, int x_offset, int color, SpriteManager sm) {
		this(scr, stack, x_offset, color, sm, 3);
	}

	public StackedRenderHandle(Screen scr, PoseStack stack, int x_offset, int color, SpriteManager sm, int ty) {
		this.font = Minecraft.getInstance().font;
		this.stack = stack;
		this.scr = scr;
		this.sm = sm;
		this.text_color = color;
		this.text_x_offset = x_offset;
		this.TEXT_Y_OFFSET = ty;
		this.TEXT_HEIGHT = TEXT_BASE_HEIGHT + ty + 2;
	}

	public void drawText(Component text) {
		endCell();
		int y = current_y + TEXT_Y_OFFSET;
		textList.add(new TextEntry(stack, text, text_x_offset, y, text_color));
		current_y += TEXT_HEIGHT;
	}

	public void drawTable(Component[][] table, int x_max) {
		endCell();
		int h = table.length;
		int w = table[0].length;
		int w1 = 0;
		int ws = 0;
		for (Component[] c : table) {
			w1 = Math.max(w1, font.width(c[0]));
			for (int i = 1; i < w; i++) {
				ws = Math.max(ws, font.width(c[i]));
			}
		}
		int sumw = w1 + ws * (w - 1);
		int x0 = text_x_offset;
		int x1 = x_max - text_x_offset;
		float space = (x1 - x0 - sumw) * 1.0f / (w - 1);
		for (Component[] c : table) {
			int y = current_y + TEXT_Y_OFFSET;
			float x_start = x0;
			for (int i = 0; i < w; i++) {
				float wi = i == 0 ? w1 : ws;
				int x = Math.round(x_start);
				textList.add(new TextEntry(stack, c[i], x, y, text_color));
				x_start += wi + space;
			}
			current_y += TEXT_HEIGHT;
		}
	}

	public Pair<CellEntry, CellEntry> drawTextWithButtons(Component text, boolean p1, boolean p2) {
		endCell();
		int y = current_y + TEXT_Y_OFFSET;
		textList.add(new TextEntry(stack, text, text_x_offset, y, text_color));
		int x_off = text_x_offset + font.width(text) + BTN_X_OFFSET;
		SpriteManager.Rect r = sm.getSide(p1 ? "button_1" : "button_1p");
		scr.blit(stack, x_off, y, r.x, r.y, r.w, r.h);
		CellEntry c1 = new CellEntry(x_off, y);
		x_off += r.w + BTN_X_OFFSET;
		r = sm.getSide(p2 ? "button_2" : "button_2p");
		scr.blit(stack, x_off, y, r.x, r.y, r.w, r.h);
		CellEntry c2 = new CellEntry(x_off, y);
		current_y += TEXT_HEIGHT;
		return Pair.of(c1, c2);
	}

	public CellEntry addCell(boolean toggled, boolean disabled) {
		startCell();
		int index = toggled ? 1 : disabled ? 2 : 0;
		int x = SLOT_X_OFFSET + current_x * SLOT_SIZE;
		int u = SPRITE_OFFSET + index * SLOT_SIZE;
		scr.blit(stack, x, current_y, u, 0, SLOT_SIZE, SLOT_SIZE);
		var ans = new CellEntry(x + 1, current_y + 1);
		current_x++;
		if (current_x == 9) {
			endCell();
		}
		return ans;
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

	public void flushText() {
		textList.forEach(e -> font.draw(e.stack, e.text, e.x, e.y, e.color));
	}

	record TextEntry(PoseStack stack, Component text, int x, int y, int color) {
	}

	record CellEntry(int x, int y) {

	}

}
