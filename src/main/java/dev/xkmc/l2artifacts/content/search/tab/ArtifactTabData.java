package dev.xkmc.l2artifacts.content.search.tab;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;
import dev.xkmc.l2tabs.tabs.core.TabToken;

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

	@Override
	public List<TabToken<ArtifactTabData, ?>> getTabs() {
		List<TabToken<ArtifactTabData, ?>> ans = new ArrayList<>();
		for (var e : advanced ? ArtifactTabRegistry.LIST_1 : ArtifactTabRegistry.LIST_0) {
			ans.add(e.get());
		}
		return ans;
	}

	public ArtifactTabData copy() {
		return this;//TODO
	}
}
