package dahminh.overloadedorigins.entity.client;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.entity.custom.ShadowDecoyEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ShadowDecoyRenderer extends MobEntityRenderer<ShadowDecoyEntity, ShadowDecoyModel<ShadowDecoyEntity>> {
    private static final Identifier TEXTURE = new Identifier(OverloadedOrigins.MOD_ID, "textures/entity/shadow_decoy.png");
    public ShadowDecoyRenderer(EntityRendererFactory.Context context) {
        super(context, new ShadowDecoyModel<>(context.getPart(OOModelLayers.SHADOW_DECOY)), 0.0f);
    }

    @Override
    public Identifier getTexture(ShadowDecoyEntity entity) {
        return TEXTURE;
    }
}
