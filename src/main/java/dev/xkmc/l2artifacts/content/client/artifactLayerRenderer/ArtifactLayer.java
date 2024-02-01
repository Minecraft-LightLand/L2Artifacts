package dev.xkmc.l2artifacts.content.client.artifactLayerRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.particle.BreakingParticle;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class ArtifactLayer extends ItemInHandLayer {
    float scaleX ;
    float scaleY ;
    float scaleZ ;
    ItemInHandRenderer itemInHandRenderer;

    public ArtifactLayer(RenderLayerParent parent, EntityModelSet set, ItemInHandRenderer renderer) {
        this(parent, set, 1.0F, 1.0F, 1.0F, renderer);
    }

    public ArtifactLayer(RenderLayerParent parent, EntityModelSet set, float x, float y, float z, ItemInHandRenderer renderer) {
        super(parent, renderer);
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
        this.itemInHandRenderer = renderer;
    }


    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int i, @NotNull LivingEntity entity, float f0, float f1, float f2, float f3, float f4, float f5) {
        if (!(entity instanceof Player) || Minecraft.getInstance().isPaused())
            return;
        Level level = entity.level();
        List<SlotResult> slots = CuriosApi.getCuriosHelper().findCurios(entity, stack -> stack.getItem() instanceof BaseArtifact);
        if (!slots.isEmpty()) {
            AtomicInteger flag = new AtomicInteger(1);//若为1则一件装备都没渲染
            for (SlotResult sr : slots) {
                if (flag.get() == 1) {
                    ItemStack stack = sr.stack();
                    if (!sr.slotContext().visible()) continue;//若该slot不可见
                    BaseArtifact base = (BaseArtifact) stack.getItem();
                    base.set.get().getCountAndIndex(sr.slotContext()).ifPresent(result -> {
                        var list = base.set.get().addComponentss(result);
                        for (var pair : list) {
                            if (sr.slotContext().visible()) {
                                flag.set(0);//渲染装备，修改为0
                                pose.pushPose();
                                pose.translate(0, (-0.62d -1.1 - entity.getHealth() / entity.getMaxHealth() - Math.sin((double) entity.level().getDayTime() / 20) / 8), -0.5D);//-0.62d-0.5-Math.sin((double)entity.level().getDayTime()/20)/8-0.125
                                pose.rotateAround(Axis.ZP.rotationDegrees(135F), 0, 0.82f, 0);
                                pose.scale(this.scaleX , this.scaleY, this.scaleZ);
                                this.itemInHandRenderer.renderItem(entity, stack, ItemDisplayContext.HEAD, false, pose, buffer, i);
                                pose.popPose();
                            }
                        }
                    });
                }
            }

            if (level.getDayTime()%5==0&&Minecraft.getInstance().getPartialTick()<=0.1 && flag.get() == 0) {
                Random aa = new Random();
                double xx = aa.nextDouble() *2-1 ;
                double zz = aa.nextDouble() *2-0.8;
                double yy = aa.nextDouble() *2-1 + 5;
                if(entity.level().getDayTime() %5 !=0 &&xx * xx + zz*zz > 1) return;
                ParticleProvider<ItemParticleOption> provider = new BreakingParticle.Provider();
                Particle particle = provider.createParticle(new ItemParticleOption(ParticleTypes.ITEM, ArtifactItemRegistry.SELECT.asStack()),(ClientLevel) level,entity.getX()+xx,entity.getY()+yy,entity.getZ()+zz,0,0,0);
                assert particle != null;
                Minecraft.getInstance().particleEngine.add(particle);


            }

        }

    }
}

