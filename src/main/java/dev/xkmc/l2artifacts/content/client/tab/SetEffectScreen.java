package dev.xkmc.l2artifacts.content.client.tab;

import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2tabs.init.data.L2TabsConfig;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SetEffectScreen extends BaseTextScreen {

	private final int page;

	protected SetEffectScreen(Component title) {
		this(title, 0);
	}

	protected SetEffectScreen(Component title, int page) {
		super(title, ResourceLocation.fromNamespaceAndPath("l2tabs", "textures/gui/empty.png"));
		this.page = page;
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, ArtifactTabRegistry.TAB_SET_EFFECTS.get());
		Player player = Proxy.getClientPlayer();
		var all = SetEffectEntries.aggregate(player, imageWidth - 16, L2TabsConfig.CLIENT.attributeLinePerPage.get());
		int totalPage = Math.max(1, all.size());
		int x = (this.width + this.imageWidth) / 2 - 16;
		int y = (this.height - this.imageHeight) / 2 + 4;
		int w = 10;
		int h = 11;
		if (this.page > 0) {
			this.addRenderableWidget(Button.builder(Component.literal("<"), (e) -> {
				this.click(-1);
			}).pos(x - w - 1, y).size(w, h).build());
		}

		if (this.page < totalPage - 1) {
			this.addRenderableWidget(Button.builder(Component.literal(">"), (e) -> {
				this.click(1);
			}).pos(x, y).size(w, h).build());
		}
	}

	private void click(int btn) {
		Minecraft.getInstance().setScreen(new SetEffectScreen(this.getTitle(), this.page + btn));
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float ptick) {
		super.render(g, mx, my, ptick);
		Player player = Proxy.getClientPlayer();
		int x = leftPos + 8;
		int y = topPos + 6;
		var all = SetEffectEntries.aggregate(player, imageWidth - 16, L2TabsConfig.CLIENT.attributeLinePerPage.get());
		List<FormattedCharSequence> seq = new ArrayList<>();
		List<Component> hover = null;
		if (page < 0 || page >= all.size()) return;
		for (var e : all.get(page)) {
			for (var pair : e.text()) {
				var lines = pair.getFirst();
				int row = topPos + 6 + seq.size() * 10;
				int column = leftPos + 6;
				seq.addAll(lines);
				int rowAft = topPos + 6 + seq.size() * 10;
				int columnAft = leftPos + 6 + lines.size() * 45;
				if (my >= row && my < rowAft && mx >= column && mx < columnAft) {
					hover = pair.getSecond();
				}
			}
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
