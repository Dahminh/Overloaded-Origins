package dahminh.overloadedorigins.mixin;

import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.util.Checks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"))
    private LivingEntity modifyTarget(LivingEntity target) {
        if (getWorld().isClient() || !(target instanceof PlayerEntity)) {
            return target;
        }
        if (target.hasStatusEffect(OOEffects.SHADOW_CLOAK) || Checks.assassinInvisibilityCheck(target)) return null;
        if (this.getCommandTags().contains("Decoy") && Checks.assassinOriginCheck(target)) return null;
        return target;
    }
}
