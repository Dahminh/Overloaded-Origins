package dahminh.moreoriginsmod.mixin;

import dahminh.moreoriginsmod.effect.CustomEffects;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
}
