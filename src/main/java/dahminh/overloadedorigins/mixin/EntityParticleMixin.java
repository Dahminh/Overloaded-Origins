package dahminh.overloadedorigins.mixin;

import dahminh.overloadedorigins.effect.OOEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class EntityParticleMixin extends Entity {

    private final DustColorTransitionParticleEffect particleEffect = new DustColorTransitionParticleEffect(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.15f, 0.15f, 0.15f), 2);
    public EntityParticleMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.hasStatusEffect(OOEffects.SHADOWCLOAK)) {
            this.setInvisible(true);
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            boolean firstPerson = MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
            if (((Object) this != player || (!firstPerson))) {
                for (int i = 0; i<2; i++) {
                    getWorld().addParticle(particleEffect,
                            this.getX() + this.random.nextGaussian() * 0.3,
                            this.getY() + 0.1 + this.random.nextGaussian() * 0.1,
                            this.getZ() + this.random.nextGaussian() * 0.3,
                            0, 0, 0);
                }
            }
        }
    }
}