package dev.xkmc.l2artifacts.content.client.tab;

import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.ArtifactClient;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.TextWrapper;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
		super(title, new ResourceLocation("l2tabs:textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager(this).init(this::addRenderableWidget, ArtifactClient.TAB_SET_EFFECTS);
	}

	@Nullable
	private List<Component> hover = null;

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		Player player = Proxy.getClientPlayer();
		int x = leftPos + 8;
		int y = topPos + 6;
		List<SlotResult> slots = CuriosApi.getCuriosHelper().findCurios(player, stack -> stack.getItem() instanceof BaseArtifact);
		List<FormattedCharSequence> seq = new ArrayList<>();
		hover = null;
		for (SlotResult sr : slots) {
			ItemStack stack = sr.stack();//玩家身上的饰品
			BaseArtifact base = (BaseArtifact) stack.getItem();
			base.set.get().getCountAndIndex(sr.slotContext()).ifPresent(result -> {
				if (result.current_index() > 0) {//第一件即可获取该套装全部信息
					return;
				}

				var list = base.set.get().addComponents(result);
				for (var pair : list) {
					var lines = TextWrapper.wrapText(Minecraft.getInstance().font, pair.getFirst(), imageWidth - 16);
					int row = topPos + 6 + seq.size() * 10;
					int column = leftPos + 6 ;
					seq.addAll(lines);
					int rowAft = topPos + 6 + seq.size() * 10;
					int columnAft = leftPos + 6 + lines.size() *45 ;
					if (my >= row && my < rowAft && mx >=column && mx <columnAft) {
						hover = pair.getSecond();
					}
				}
			});

		}
		for (var comp : seq) {
			g.drawString(font, comp, x, y, 0, false);
			y += 10;
		}
		if (hover != null) {
			g.renderTooltip(font, hover, Optional.empty(), mx, my);
		}
	}

}
