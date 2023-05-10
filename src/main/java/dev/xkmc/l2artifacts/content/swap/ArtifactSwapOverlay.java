package dev.xkmc.l2artifacts.content.swap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.overlay.OverlayUtils;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ArtifactSwapOverlay extends SelectionSideBar {

	public static final ArtifactSwapOverlay INSTANCE = new ArtifactSwapOverlay();

	private ArtifactSwapOverlay() {
		super(40, 3);
	}

	@Override
	public Pair<List<ItemStack>, Integer> getItems() {
		return Pair.of(List.of(), 0);
	}

	@Nullable
	private ArtifactSwapData getData() {
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return null;
		ItemStack main = player.getMainHandItem();
		if (main.getItem() instanceof ArtifactSwapItem) {
			return ArtifactSwapItem.getData(main);
		}
		ItemStack off = player.getOffhandItem();
		if (off.getItem() instanceof ArtifactSwapItem) {
			return ArtifactSwapItem.getData(off);
		}
		return null;
	}

	@Override
	public boolean isAvailable(ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean onCenter() {
		return false;
	}

	@Override
	public int getSignature() {
		ArtifactSwapData data = getData();
		if (data == null) return 0;
		return data.select + 100;
	}

	@Override
	public boolean isScreenOn() {
		if (Minecraft.getInstance().screen != null) return false;
		return getData() != null;
	}

	public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		if (this.ease((float) gui.getGuiTicks() + partialTick)) {
			this.initRender();
			gui.setupOverlayRenderState(true, false);
			LocalPlayer player = Proxy.getClientPlayer();
			ArtifactSwapData data = getData();
			if (player == null || data == null) return;
			idle = 0;
			ItemRenderer renderer = gui.getMinecraft().getItemRenderer();
			Font font = gui.getMinecraft().font;
			int dx = this.getXOffset(width);
			int dy = this.getYOffset(height);
			for (int i = 0; i < 9; ++i) {
				int y = 18 * i + dy;
				for (int j = 0; j < 5; j++) {
					int x = dx + 18 * j;
					var slot = data.contents[j * 9 + i];
					ItemStack stack = slot.getStack();
					if (!slot.isLocked()) {
						this.renderSelection(x, y, data.select == i ? 128 : 64, true, data.select == i && !stack.isEmpty());
						if (!stack.isEmpty()) {
							renderer.renderAndDecorateItem(stack, x, y);
							renderer.renderGuiItemDecorations(font, stack, x, y);
						}
					}
					if (data.select == i) {
						var opt = player.getCapability(CuriosCapability.INVENTORY).resolve()
								.flatMap(cap -> cap.getStacksHandler(slot.slot.getRegistryName().getPath()))
								.map(ICurioStacksHandler::getStacks);
						if (opt.isPresent()) {
							int cx = dx + 18 * 6;
							int cy = dy + 36 + j * 18;
							ItemStack current = opt.get().getStackInSlot(0);
							boolean sel = !slot.isLocked() && (!current.isEmpty() || !stack.isEmpty());
							this.renderArmorSlot(cx, cy, 64, sel && !current.isEmpty());
							if (!current.isEmpty()) {
								renderer.renderAndDecorateItem(current, cx, cy);
								renderer.renderGuiItemDecorations(font, current, cx, cy);
							}
						}
					}
				}
			}

		}
	}

	public void renderArmorSlot(int x, int y, int a, boolean target) {
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tesselator tex = Tesselator.getInstance();
		BufferBuilder builder = tex.getBuilder();
		OverlayUtils.fillRect(builder, x, y, 16, 16, 255, 255, 255, a);
		if (target) {
			OverlayUtils.drawRect(builder, x, y, 16, 16, 70, 150, 185, 255);
		}
		RenderSystem.enableDepthTest();
	}


	protected int getXOffset(int width) {
		float progress = (this.max_ease - this.ease_time) / this.max_ease;
		return this.onCenter() ? width / 2 - 54 - 1 - Math.round(progress * (float) width / 2.0F) : 2 - Math.round(progress * 20.0F);
	}

	protected int getYOffset(int height) {
		return height / 2 - 9 * 9 + 1;
	}

}
