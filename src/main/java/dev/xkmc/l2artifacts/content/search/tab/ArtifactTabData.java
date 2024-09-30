package dev.xkmc.l2artifacts.content.search.tab;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ArtifactTabData extends TabGroupData<ArtifactTabData> {

	public final boolean advanced;
	public final ArtifactChestToken token;

	public ArtifactTabData(boolean advanced, ArtifactChestToken token) {
		super(ArtifactTabRegistry.ARTIFACT);
		this.advanced = advanced;
		this.token = token;
	}

	public static ArtifactTabData of(Player player, int i) {
	}

	@Override
	public List<TabToken<ArtifactTabData, ?>> getTabs() {
		return null;
	}

	public ArtifactTabData copy() {
		return this;//TODO
	}
}
