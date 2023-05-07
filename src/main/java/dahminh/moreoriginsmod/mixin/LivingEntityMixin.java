package dahminh.moreoriginsmod.mixin;

import dahminh.moreoriginsmod.effect.CustomEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "heal", at = @At("HEAD"), index = 1)
    private float modifyHealing(float amount){
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(CustomEffects.SHADOWBETRAYAL)) {
            return (float) (amount * 0.5);
        }
        return amount;
    }

    @Inject(at = @At("HEAD"), method= "onAttacking")
    private void onAttacking(Entity target, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(CustomEffects.SHADOWCLOAK) & !target.getScoreboardTags().contains("Decoy")) {
            self.removeStatusEffect(CustomEffects.SHADOWCLOAK);
        }
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.hasStatusEffect(CustomEffects.SHADOWCLOAK)) {
            self.removeStatusEffect(CustomEffects.SHADOWCLOAK);
        }
    }
}
