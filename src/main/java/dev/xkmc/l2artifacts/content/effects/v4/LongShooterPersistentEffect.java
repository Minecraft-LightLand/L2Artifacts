package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

public class LongShooterPersistentEffect extends PersistentDataSetEffect<SetEffectData> {

	public LongShooterPersistentEffect() {
		super();
	}

	@Override
	public List<MutableComponent> getDetailedDescription(int rank) {
		List<MutableComponent> ans = new ArrayList<>();
		ans.add(getConditionText(rank));
		return ans;
	}

	@Override
	public SetEffectData getData(ArtifactSetConfig.Entry ent) {
		return new SetEffectData();
	}

	protected MutableComponent getConditionText(int rank) {

		return Component.translatable(getDescriptionId() + ".desc");
	}
}
