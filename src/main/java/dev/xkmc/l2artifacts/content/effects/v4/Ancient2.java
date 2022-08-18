package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;


public class Ancient2 extends PersistentDataSetEffect<AncientData2> {
    private final AttrSetEntry[] entries;
    private final LinearFuncEntry level, threshold;

    public Ancient2(LinearFuncEntry level, LinearFuncEntry threshold,AttrSetEntry...entries) {
        super(0);
        this.level = level;
        this.threshold = threshold;
        this.entries = entries;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (!enabled) return;
        AncientData2 data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
        data.update(3, rank);
        if (!player.isSprinting()) {
            data.time++;
            if(data.time>=threshold.getFromRank(rank)){
                player.heal((float) level.getFromRank(rank));
                data.time = 0;
            }
        } else {
            data.time = 0;
        }
    }

    @Override
    public AncientData2 getData(ArtifactSetConfig.Entry ent) {
        return new AncientData2();
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int prot = (int) Math.round(level.getFromRank(rank) );
        return List.of(Component.translatable(getDescriptionId() + ".desc", prot));
    }


}
