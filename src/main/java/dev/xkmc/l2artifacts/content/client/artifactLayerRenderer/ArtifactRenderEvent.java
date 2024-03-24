package dev.xkmc.l2artifacts.content.client.artifactLayerRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Iterator;
import java.util.Map;

public class ArtifactRenderEvent {
    private static final String REG_NAME = "artifact";
    public static final ModelLayerLocation ARTIFACT_LAYER = new ModelLayerLocation(new ResourceLocation("l2artifacts", "lollipop"), "main");

    public ArtifactRenderEvent() {
    }

    public static void registerArtifactLayer() {
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        Map<String, EntityRenderer<? extends Player>> skinMap = renderManager.getSkinMap();
        Iterator var2 = skinMap.values().iterator();

        while(var2.hasNext()) {
            EntityRenderer<? extends Player> renderer = (EntityRenderer)var2.next();
            if (renderer instanceof LivingEntityRenderer) {
                LivingEntityRenderer ler = (LivingEntityRenderer)renderer;
                addLayer(renderManager, ler);
            }
        }

    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>> void addLayer(EntityRenderDispatcher manager, LivingEntityRenderer<T, M> ler) {
        Minecraft mc = Minecraft.getInstance();
        //TODO ler.addLayer(new ArtifactLayer(ler, mc.getEntityModels(), manager.getItemInHandRenderer()));
    }

}
