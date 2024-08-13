package dahminh.overloadedorigins;

import dahminh.overloadedorigins.entity.OOEntities;
import dahminh.overloadedorigins.entity.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;

public class OverloadedOriginsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(OOEntities.SPECTRE, SpectreRenderer::new);
        EntityRendererRegistry.register(OOEntities.SHADOW_DECOY, ShadowDecoyRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(OOModelLayers.SPECTRE, SpectreModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(OOModelLayers.SHADOW_DECOY, ShadowDecoyModel::getTexturedModelData);
    }
}
