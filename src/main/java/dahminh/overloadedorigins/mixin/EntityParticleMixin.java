package dahminh.overloadedorigins.mixin;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.power.ParticlePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class EntityParticleMixin extends Entity {

    @Shadow public abstract int getXpToDrop();

    @Unique
    private final DustColorTransitionParticleEffect PARTICLE_EFFECT = new DustColorTransitionParticleEffect(
            new Vector3f(0.0f, 0.0f, 0.0f),
            new Vector3f(0.15f, 0.15f, 0.15f),
            2
    );

    public EntityParticleMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        shadowCloakParticles(livingEntity);
        particlePower();
    }

    @Unique
    private void shadowCloakParticles(LivingEntity livingEntity) {
        if (!livingEntity.hasStatusEffect(OOEffects.SHADOW_CLOAK)) return;

        this.setInvisible(true);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        boolean firstPerson = MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
        if (((Object) this != player || (!firstPerson))) {
            for (int i = 0; i<2; i++) {
                getWorld().addParticle(PARTICLE_EFFECT,
                        this.getX() + this.random.nextGaussian() * 0.3,
                        this.getY() + 0.1 + this.random.nextGaussian() * 0.1,
                        this.getZ() + this.random.nextGaussian() * 0.3,
                        0, 0, 0);
            }
        }
    }

    @Unique
    private void particlePower() {

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        boolean inFirstPerson = MinecraftClient.getInstance().options.getPerspective().isFirstPerson();

        if (player == null) {
            return;
        }

        double velocityX;
        double velocityY;
        double velocityZ;

        for (ParticlePower particlePower : PowerHolderComponent.getPowers(this, ParticlePower.class)) {

            if (!particlePower.doesApply(player, inFirstPerson)) {
                continue;
            }

            Vec3d spread = particlePower
                    .getSpread()
                    .multiply(this.getWidth(), this.getEyeHeight(this.getPose()), this.getWidth());

            String y_type = particlePower.getYType();
            double scale = particlePower.getScale();
            Vec3d particlePos = switch (y_type) {
                case "body" -> (new Vec3d(
                        this.getX(),
                        this.getBodyY(scale),
                        this.getZ())
                    ).add(particlePower.getOffsetX(), particlePower.getOffsetY(), particlePower.getOffsetZ());
                case "eyes" -> (new Vec3d(
                        this.getX(),
                        this.getEyeY(),
                        this.getZ())
                    ).add(particlePower.getOffsetX(), particlePower.getOffsetY(), particlePower.getOffsetZ());
                default -> this
                        .getPos()
                        .add(particlePower.getOffsetX(), particlePower.getOffsetY(), particlePower.getOffsetZ());
            };


            if (particlePower.getCount() == 0) {

                velocityX = spread.getX() * particlePower.getSpeed();
                velocityY = spread.getY() * particlePower.getSpeed();
                velocityZ = spread.getZ() * particlePower.getSpeed();

                this.getWorld().addParticle(particlePower.getParticle(), particlePower.shouldForce(), particlePos.getX(), particlePos.getY(), particlePos.getZ(), velocityX, velocityY, velocityZ);

            } else {

                for (int i = 0; i < particlePower.getCount(); i++) {

                    Vec3d newSpread = spread.multiply(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());
                    Vec3d newParticlePos = particlePos.add(newSpread);

                    velocityX = (2.0 * this.random.nextDouble() - 1.0) * particlePower.getSpeed();
                    velocityY = (2.0 * this.random.nextDouble() - 1.0) * particlePower.getSpeed();
                    velocityZ = (2.0 * this.random.nextDouble() - 1.0) * particlePower.getSpeed();

                    this.getWorld().addParticle(particlePower.getParticle(), particlePower.shouldForce(), newParticlePos.getX(), newParticlePos.getY(), newParticlePos.getZ(), velocityX, velocityY, velocityZ);

                }

            }

        }

    }


}