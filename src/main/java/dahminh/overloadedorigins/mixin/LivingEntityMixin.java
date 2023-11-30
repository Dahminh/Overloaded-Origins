package dahminh.overloadedorigins.mixin;

import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.sound.OOSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "heal", at = @At("HEAD"), index = 1)
    private float modifyHealing(float amount){
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(OOEffects.SHADOW_BETRAYAL)) return (float) (amount * 0.5);
        return amount;
    }

    @Inject(at = @At("HEAD"), method= "onAttacking")
    private void onAttacking(Entity target, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(OOEffects.SHADOW_CLOAK) & !target.getCommandTags().contains("Decoy")) {
            self.removeStatusEffect(OOEffects.SHADOW_CLOAK);
        }
    }

    @Inject(at = @At("TAIL"), method = "damage")
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(OOEffects.SHADOW_CLOAK) && amount > 0) {
            self.removeStatusEffect(OOEffects.SHADOW_CLOAK);
        }
    }

    @Inject(at = @At("TAIL"), method = "onStatusEffectRemoved")
    private void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        LivingEntity e = (LivingEntity) (Object) this;
        if (e.getWorld().isClient) {
            return;
        }
        if (effect.getEffectType().equals(OOEffects.SHADOW_CLOAK)) {
            e.getWorld().playSound(null, e.getX(), e.getY(), e.getZ(), OOSounds.DARK_ELF_APPEARS, SoundCategory.HOSTILE, 1.0f, 2.0f);
            ((ServerWorld) e.getWorld()).spawnParticles(
                    LARGE_SMOKE,
                    e.getX(),
                    e.getY(),
                    e.getZ(),
                    25,
                    0.5,
                    0,
                    0.5,
                    0
            );
        }
    }
}
