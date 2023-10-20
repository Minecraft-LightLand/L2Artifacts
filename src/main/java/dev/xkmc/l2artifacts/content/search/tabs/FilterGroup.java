package dev.xkmc.l2artifacts.content.search.tabs;

import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2tabs.tabs.core.TabGroup;
import dev.xkmc.l2tabs.tabs.core.TabGroupData;

public class FilterGroup extends TabGroupData<FilterGroup> {

	public final ArtifactChestToken token;

	public FilterGroup(TabGroup<FilterGroup> group, ArtifactChestToken token) {
		super(group);
		this.token = token;
	}

}
