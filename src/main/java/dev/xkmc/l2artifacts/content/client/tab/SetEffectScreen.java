package dev.xkmc.l2artifacts.content.client.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.ModClient;
import dev.xkmc.l2library.base.tabs.contents.BaseTextScreen;
import dev.xkmc.l2library.base.tabs.core.TabManager;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.TextWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SetEffectScreen extends BaseTextScreen {

	protected SetEffectScreen(Component title) {
		super(title, new ResourceLocation("l2library:textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, ModClient.TAB_SET_EFFECTS);
	}

	@Nullable
	private List<Component> hover = null;

	@Override
	public void render(PoseStack pose, int mx, int my, float ptick) {
		super.render(pose, mx, my, ptick);
		Player player = Proxy.getClientPlayer();
		int x = leftPos + 8;
		int y = topPos + 6;
		List<SlotResult> slots = CuriosApi.getCuriosHelper().findCurios(player, stack -> stack.getItem() instanceof BaseArtifact);
		List<FormattedCharSequence> seq = new ArrayList<>();
		hover = null;
		for (SlotResult sr : slots) {
			ItemStack stack = sr.stack();
			BaseArtifact base = (BaseArtifact) stack.getItem();
			base.set.get().getCountAndIndex(sr.slotContext()).ifPresent(result -> {
				if (result.current_index() > 0) {
					return;
				}
				var list = base.set.get().addComponents(result);
				for (var pair : list) {
					var lines = TextWrapper.wrapText(Minecraft.getInstance().font, pair.getFirst(), imageWidth - 16);
					int row = topPos + 6 + seq.size() * 10;
					seq.addAll(lines);
					int rowAft = topPos + 6 + seq.size() * 10;
					if (my >= row && my < rowAft) {
						hover = pair.getSecond();
					}
				}
			});
		}
		for (var comp : seq) {
			this.font.draw(pose, comp, x, y, 0);
			y += 10;
		}
		if (hover != null) {
			renderTooltip(pose, hover, Optional.empty(), mx, my);
		}
	}

}
