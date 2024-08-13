// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package dahminh.overloadedorigins.entity.client;

import dahminh.overloadedorigins.entity.custom.SpectreEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SpectreModel<T extends SpectreEntity> extends Model {
	private final ModelPart spectre;
	private final ModelPart head;

	public SpectreModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.spectre = root.getChild("spectre");
		this.head    = spectre.getChild("head_all");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spectre = modelPartData.addChild("spectre", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head_all = spectre.addChild("head_all", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -19.0F, 0.0F));

		ModelPartData headwear = head_all.addChild("headwear", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 1.0F));

		ModelPartData head = head_all.addChild("head", ModelPartBuilder.create().uv(32, 2).cuboid(-4.0F, -10.0F, 1.0F, 8.0F, 8.0F, 6.0F, new Dilation(-0.005F))
				.uv(60, 2).cuboid(-2.0F, -5.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.005F))
				.uv(60, 2).mirrored().cuboid(3.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(0, 1).mirrored().cuboid(1.0F, -10.0F, 0.0F, 3.0F, 1.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(24, 0).mirrored().cuboid(1.0F, -9.0F, 0.0F, 3.0F, 2.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(60, 13).mirrored().cuboid(3.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(60, 13).mirrored().cuboid(2.0F, -6.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(1.0F, -5.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.005F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(3.0F, -3.75F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(2.0F, -4.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(1.25F, -3.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(0.25F, -3.75F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)).mirrored(false)
				.uv(60, 2).mirrored().cuboid(2.75F, -5.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)).mirrored(false)
				.uv(24, 0).cuboid(-4.0F, -9.0F, 0.0F, 3.0F, 2.0F, 1.0F, new Dilation(-0.005F))
				.uv(60, 13).cuboid(-4.0F, -7.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.005F))
				.uv(60, 13).cuboid(-3.0F, -6.0F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.005F))
				.uv(58, 16).cuboid(-1.0F, -10.0F, 0.0F, 2.0F, 6.0F, 1.0F, new Dilation(-0.005F))
				.uv(60, 2).cuboid(-4.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.005F))
				.uv(0, 1).cuboid(-4.0F, -10.0F, 0.0F, 3.0F, 1.0F, 1.0F, new Dilation(-0.005F))
				.uv(0, 16).cuboid(-4.0F, -2.75F, 0.0F, 8.0F, 1.0F, 2.0F, new Dilation(-0.25F))
				.uv(60, 2).cuboid(-4.0F, -3.75F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
				.uv(60, 2).cuboid(-3.75F, -5.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
				.uv(60, 2).cuboid(-2.25F, -3.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
				.uv(60, 2).cuboid(-3.0F, -4.25F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
				.uv(60, 2).cuboid(-1.25F, -3.75F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
				.uv(56, 0).cuboid(-1.5F, -4.25F, 0.25F, 3.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(0.0F, 2.0F, -3.0F));

		ModelPartData body = spectre.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -3.0F));

		ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(38, 17).mirrored().cuboid(-2.0F, -2.0F, -1.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
				.uv(38, 17).cuboid(-8.0F, -2.0F, -1.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
				.uv(22, 17).cuboid(-6.0F, -2.0F, -1.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -6.0F, 5.5F, 0.5672F, 0.0F, 0.0F));

		ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(2, 21).cuboid(-4.0F, -10.0F, -1.0F, 8.0F, 13.0F, 4.0F, new Dilation(-0.005F)), ModelTransform.of(0.0F, -10.0F, 4.0F, 0.1309F, 0.0F, 0.0F));

		ModelPartData left_arm = spectre.addChild("left_arm", ModelPartBuilder.create().uv(32, 24).cuboid(4.0F, -18.75F, -7.0F, 4.0F, 4.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -3.0F));

		ModelPartData right_arm = spectre.addChild("right_arm", ModelPartBuilder.create().uv(32, 24).mirrored().cuboid(-8.0F, -18.75F, -7.0F, 4.0F, 4.0F, 12.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, -3.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.spectre.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	public void setRotation(float yaw, float pitch) {
		this.spectre.yaw = yaw * ((float)Math.PI / 180);
		this.spectre.pitch = pitch * ((float)Math.PI / 180);
		//this.setHeadRotation(yaw, pitch);
	}

	public void setHeadRotation(float yaw, float pitch) {
		yaw = MathHelper.clamp(yaw, -30.0F, 30.0F);
		pitch = MathHelper.clamp(pitch, -25.0F, 45.0F);

		this.head.yaw   = yaw * 0.017453292F;
		this.head.pitch = pitch * 0.017453292F;
	}
}