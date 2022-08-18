package dev.xkmc.l2artifacts.content.effects.v4;


import dev.xkmc.l2artifacts.content.capability.ArtifactData;
import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractCASetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Ancient1 extends AbstractCASetEffect<AncientData1> {
    private final LinearFuncEntry speed, threshold;

    public Ancient1(LinearFuncEntry speed, LinearFuncEntry threshold,AttrSetEntry...entries) {
        super(entries);
        this.speed = speed;
        this.threshold = threshold;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (!enabled) return;
        AncientData1 data = ArtifactData.HOLDER.get(player).getOrCreateData(this, ent);
        data.update(3, rank);
        if (player.isSprinting()) {
            data.time++;
            if(data.time>=threshold.getFromRank(rank)){
                addAttributes(player,ent,rank,data);
                data.time = 0;
            }
        } else {
            data.time = 0;
        }
    }



    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int prot = (int) Math.round(speed.getFromRank(rank) * 100);
        return List.of(Component.translatable(getDescriptionId() + ".desc", prot));
    }


    @Override
    protected AncientData1 getData() {
        return new AncientData1();
    }
}
