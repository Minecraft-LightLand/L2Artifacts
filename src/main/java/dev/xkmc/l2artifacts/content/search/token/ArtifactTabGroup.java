package dev.xkmc.l2artifacts.content.search.token;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabSprites;
import dev.xkmc.l2tabs.tabs.core.TabType;

public class ArtifactTabGroup extends TabGroup<ArtifactTabData> {

	public final static TabSprites RIGHT = new TabSprites(32, 28,
			L2Artifacts.loc("right/des_0"),
			L2Artifacts.loc("right/des_1"),
			L2Artifacts.loc("right/des_2"),
			L2Artifacts.loc("right/sel_0"),
			L2Artifacts.loc("right/sel_1"),
			L2Artifacts.loc("right/sel_2")
	);

	public ArtifactTabGroup(TabType type, int max, boolean enableLast) {
		super(type, max, enableLast);
	}

	@Override
	public TabSprites texture() {
		return RIGHT;
	}
}
