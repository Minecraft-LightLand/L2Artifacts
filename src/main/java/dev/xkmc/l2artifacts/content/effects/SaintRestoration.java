package dev.xkmc.l2artifacts.content.effects;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class SaintRestoration extends SetEffect {

	private final int base, slope;

	public SaintRestoration(int max_base, int max_slope) {
		super(0);
		this.base = max_base;
		this.slope = max_slope;
	}

	@Override
	public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
		if (!enabled)
			return;
		int period = base - (rank - 1) * slope;
		if (player.tickCount % period == 0 && player.getMainHandItem().isEmpty()) {
			if (player.getHealth() < player.getMaxHealth()) {
				player.heal(1);
			} else {
				List<Player> list = player.level.getEntitiesOfClass(Player.class, player.getBoundingBox().inflate(32));
				Player min = null;
				double health = 1e6;
				for (Player p : list) {
					if (p.isAlive() && p.getHealth() < health) {
						min = p;
						health = p.getHealth();
					}
				}
				if (min != null) {
					min.heal(1);
				}
			}
		}
	}

	@Override
	public List<MutableComponent> getDetailedDescription(BaseArtifact item) {
		double period = (base - (item.rank - 1) * slope) / 20d;
		return List.of(MutableComponent.create(new TranslatableContents(getDescriptionId() + ".desc", ATTRIBUTE_MODIFIER_FORMAT.format(period))));
	}

}
