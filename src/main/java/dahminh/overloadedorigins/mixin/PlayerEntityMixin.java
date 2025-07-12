package dahminh.overloadedorigins.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.util.Checks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean assassinate(boolean criticalHit, Entity target) {
        LivingEntity living;
        if (target instanceof LivingEntity) living = (LivingEntity) target;
        else return criticalHit;

        boolean isTargetFullHealth   = living.getHealth() / living.getMaxHealth() == 1.0;
        boolean isAssassinOrigin     = Checks.originCheck(this, Checks.OriginsTiers.ASSASSIN);
        boolean hasGlowingEffect     = this.hasStatusEffect(StatusEffects.GLOWING);
        boolean hasShadowCloakEffect = this.hasStatusEffect(OOEffects.SHADOW_CLOAK);
        boolean canAssassinate       = isTargetFullHealth && isAssassinOrigin && !hasGlowingEffect && hasShadowCloakEffect;

        return canAssassinate || criticalHit;
    }
}
