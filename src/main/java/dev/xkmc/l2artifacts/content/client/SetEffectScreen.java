package dev.xkmc.l2artifacts.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.ModClient;
import dev.xkmc.l2artifacts.util.TextWrapper;
import dev.xkmc.l2library.base.tabs.contents.BaseTextScreen;
import dev.xkmc.l2library.base.tabs.core.TabManager;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.List;

public class SetEffectScreen extends BaseTextScreen {

	protected SetEffectScreen(Component title) {
		super(title, new ResourceLocation("l2library:textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, ModClient.TAB_SET_EFFECTS);
	}

	@Override
	public void render(PoseStack pose, int mx, int my, float ptick) {
		super.render(pose, mx, my, ptick);
		Player player = Proxy.getClientPlayer();
		int x = leftPos + 8;
		int y = topPos + 6;
		List<SlotResult> slots = CuriosApi.getCuriosHelper().findCurios(player, stack -> stack.getItem() instanceof BaseArtifact);
		List<Component> list = new ArrayList<>();
		for (SlotResult sr : slots) {
			ItemStack stack = sr.stack();
			BaseArtifact base = (BaseArtifact) stack.getItem();
			base.set.get().getCountAndIndex(sr.slotContext()).ifPresent(result -> {
				if (result.current_index() > 0) {
					return;
				}
				base.set.get().addComponents(list, result);
			});
		}
		var lines = TextWrapper.wrapText(Minecraft.getInstance().font, list, imageWidth - 16);
		for (var comp : lines) {
			this.font.draw(pose, comp, x, y, 0);
			y += 10;
		}
	}

}
