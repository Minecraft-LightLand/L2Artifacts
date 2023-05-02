package dev.xkmc.l2artifacts.content.search.tabs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
enum FilterTabType {
	ABOVE(0, 0, 28, 32, 8),
	BELOW(84, 0, 28, 32, 8),
	LEFT(0, 64, 32, 28, 5),
	RIGHT(96, 64, 32, 28, 5);

	public static final int MAX_TABS = 8;
	private final int textureX;
	private final int textureY;
	private final int width;
	private final int height;

	FilterTabType(int tx, int ty, int w, int h, int max) {
		this.textureX = tx;
		this.textureY = ty;
		this.width = w;
		this.height = h;
	}

	public void draw(PoseStack stack, GuiComponent screen, int x, int y, boolean selected, int index) {
		index = index % MAX_TABS;
		int tx = this.textureX;
		if (index > 0) {
			tx += this.width;
		}

		if (index == MAX_TABS - 1) {
			tx += this.width;
		}

		int ty = selected ? this.textureY + this.height : this.textureY;
		GuiComponent.blit(stack, x, y, tx, ty, this.width, this.height);
	}

	public void drawIcon(PoseStack pose, int x, int y, int index, ItemRenderer renderer, ItemStack stack) {
		int i = x;
		int j = y;
		switch (this) {
			case ABOVE -> {
				i += 6;
				j += 9;
			}
			case BELOW -> {
				i += 6;
				j += 6;
			}
			case LEFT -> {
				i += 10;
				j += 5;
			}
			case RIGHT -> {
				i += 6;
				j += 5;
			}
		}

		renderer.renderAndDecorateFakeItem(pose, stack, i, j);
	}

	public int getX(int pIndex) {
		return switch (this) {
			case ABOVE, BELOW -> (this.width + 4) * pIndex;
			case LEFT -> -this.width + 4;
			case RIGHT -> -4;
		};
	}

	public int getY(int pIndex) {
		return switch (this) {
			case ABOVE -> -this.height + 4;
			case BELOW -> -4;
			case LEFT, RIGHT -> this.height * pIndex;
		};
	}

	public boolean isMouseOver(int p_97214_, int p_97215_, int p_97216_, double p_97217_, double p_97218_) {
		int i = p_97214_ + this.getX(p_97216_);
		int j = p_97215_ + this.getY(p_97216_);
		return p_97217_ > (double) i && p_97217_ < (double) (i + this.width) && p_97218_ > (double) j && p_97218_ < (double) (j + this.height);
	}
}
