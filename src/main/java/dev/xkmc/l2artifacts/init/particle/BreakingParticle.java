package dev.xkmc.l2artifacts.init.particle;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class BreakingParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;
    BreakingParticle(ClientLevel p_105646_, double p_105647_, double p_105648_, double p_105649_, double p_105650_, double p_105651_, double p_105652_, ItemStack p_105653_) {
        this(p_105646_, p_105647_, p_105648_, p_105649_, p_105653_);
        this.xd *=  0.1F;
        this.yd *=  0.1F;
        this.zd *=  0.1F;
        this.xd += p_105650_;
        this.yd += p_105651_;
        this.zd += p_105652_;
    }



    public @NotNull ParticleRenderType getRenderType() {
       return ParticleRenderType.TERRAIN_SHEET;
    }

    protected BreakingParticle(ClientLevel p_105665_, double p_105666_, double p_105667_, double p_105668_, ItemStack p_105669_) {
        super(p_105665_, p_105666_, p_105667_, p_105668_, 0.0D, 0.0D, 0.0D);
        this.gravity = 0.5F;
        this.quadSize /= 4.0F;
        var model = Minecraft.getInstance().getItemRenderer().getModel(p_105669_, p_105665_, null, 0);
        this.setSprite(model.getOverrides().resolve(model, p_105669_, p_105665_, null, 0).getParticleIcon(net.minecraftforge.client.model.data.ModelData.EMPTY));
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.alpha= 75;
        this.setColor(255,255,255);
        this.scale(1);
        this.lifetime=90;
    }


    protected float getU0() {
        return this.sprite.getU(this.uo +5 );
    }

    protected float getU1() {
        return this.sprite.getU(this.uo +6);
    }

    protected float getV0() {
        return this.sprite.getV(this.vo +5);
    }

    protected float getV1() {
        return this.sprite.getV((this.vo + 6.0F) );
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ItemParticleOption> {
        public Particle createParticle(ItemParticleOption p_105677_, @NotNull ClientLevel p_105678_, double p_105679_, double p_105680_, double p_105681_, double p_105682_, double p_105683_, double p_105684_) {
            return new BreakingParticle(p_105678_, p_105679_, p_105680_, p_105681_, p_105682_, p_105683_, p_105684_, p_105677_.getItem());
        }
    }


}
