package dahminh.overloadedorigins.entity.client;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.entity.custom.SpectreEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SpectreRenderer extends EntityRenderer<SpectreEntity> {
    private static final Identifier TEXTURE = new Identifier(OverloadedOrigins.MOD_ID, "textures/entity/spectre.png");
    private final SpectreModel model;

    public SpectreRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SpectreModel(context.getPart(OOModelLayers.SPECTRE));
    }

    @Override
    public Identifier getTexture(SpectreEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(SpectreEntity spectreEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.translate(0.0f, -1.0f, 0.0f);
        float yaw = MathHelper.lerpAngleDegrees(g, spectreEntity.prevYaw, spectreEntity.getYaw());
        float pitch = MathHelper.lerp(g, spectreEntity.prevPitch, spectreEntity.getPitch());
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(spectreEntity)));
        this.model.setRotation(yaw, pitch);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(spectreEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
