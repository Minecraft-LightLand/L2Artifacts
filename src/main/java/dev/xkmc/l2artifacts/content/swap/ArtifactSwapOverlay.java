package dev.xkmc.l2artifacts.content.swap;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2artifacts.events.ArtifactSel;
import dev.xkmc.l2library.base.overlay.OverlayUtils;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;

public class ArtifactSwapOverlay extends SelectionSideBar<Integer, SideBar.IntSignature> {

	public static final ArtifactSwapOverlay INSTANCE = new ArtifactSwapOverlay();

	private ArtifactSwapOverlay() {
		super(40, 3);
	}

	@Override
	public Pair<List<Integer>, Integer> getItems() {
		return Pair.of(List.of(), 0);
	}

	@Override
	public boolean isAvailable(Integer integer) {
		return true;
	}

	@Override
	public boolean onCenter() {
		return false;
	}

	@Override
	public IntSignature getSignature() {
		LocalPlayer player = Proxy.getClientPlayer();
		assert player != null;
		var data = ArtifactSel.getData(player);
		if (data == null) return new IntSignature(0);
		return new IntSignature(data.second().select + 100);
	}

	@Override
	public boolean isScreenOn() {
		if (Minecraft.getInstance().screen != null) return false;
		LocalPlayer player = Proxy.getClientPlayer();
		assert player != null;
		return ArtifactSel.INSTANCE.isClientActive(player);
	}

	@Override
	public void renderContent(Context ctx) {
		LocalPlayer player = Proxy.getClientPlayer();
		if (player == null) return;
		var pair = ArtifactSel.getData(player);
		if (pair == null) return;
		var data = pair.second();
		idle = 0;
		for (int i = 0; i < 9; ++i) {
			float y = 18 * i + ctx.y0();
			for (int j = 0; j < 5; j++) {
				float x = ctx.x0() + 18 * j;
				var slot = data.contents[j * 9 + i];
				ItemStack stack = slot.getStack();
				if (!slot.isLocked()) {
					this.renderSelection(x, y, data.select == i ? 128 : 64, true, data.select == i && !stack.isEmpty());
					ctx.renderItem(stack, (int) x, (int) y);
				}
				if (data.select == i) {
					var opt = player.getCapability(CuriosCapability.INVENTORY).resolve()
							.flatMap(cap -> cap.getStacksHandler(slot.slot.getCurioIdentifier()))
							.map(ICurioStacksHandler::getStacks);
					if (opt.isPresent()) {
						float cx = ctx.x0() + 18 * 6;
						float cy = ctx.y0() + 36 + j * 18;
						ItemStack current = opt.get().getStackInSlot(0);
						boolean sel = !slot.isLocked() && (!current.isEmpty() || !stack.isEmpty());
						this.renderArmorSlot(cx, cy, 64, sel && !current.isEmpty());
						ctx.renderItem(current, (int) cx, (int) cy);
					}
				}
			}
		}
	}

	@Override
	protected void renderEntry(Context context, Integer integer, int i, int i1) {

	}

	public void renderArmorSlot(float x, float y, int a, boolean target) {
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

	public void renderSelection(float x, float y, int a, boolean available, boolean selected) {
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tesselator tex = Tesselator.getInstance();
		BufferBuilder builder = tex.getBuilder();
		if (available) {
			OverlayUtils.fillRect(builder, x, y, 16.0F, 16.0F, 255, 255, 255, a);
		} else {
			OverlayUtils.fillRect(builder, x, y, 16.0F, 16.0F, 255, 0, 0, a);
		}

		if (selected) {
			OverlayUtils.drawRect(builder, x, y, 16.0F, 16.0F, 255, 170, 0, 255);
		}

		RenderSystem.enableDepthTest();
	}

	protected float getXOffset(int width) {
		float progress = (this.max_ease - this.ease_time) / this.max_ease;
		return this.onCenter() ? width / 2f - 54 - 1 - progress * (float) width / 2.0F : 2 - progress * 20.0F;
	}

	protected float getYOffset(int height) {
		return height / 2f - 9 * 9 + 1;
	}

}
