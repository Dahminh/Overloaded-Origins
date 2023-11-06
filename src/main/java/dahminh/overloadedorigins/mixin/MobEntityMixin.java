package dahminh.overloadedorigins.mixin;

import dahminh.overloadedorigins.effect.OOEffects;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    private static final Identifier assassinT0 = new Identifier("assassin:upgraded0");
    private static final Identifier assassinT1 = new Identifier("assassin:upgraded1");
    private static final Identifier assassinT2 = new Identifier("assassin:upgraded2");
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"))
    private LivingEntity modifyTarget(LivingEntity target) {
        if (getWorld().isClient() || !(target instanceof PlayerEntity)) {
            return target;
        }
        if (target.hasStatusEffect(OOEffects.SHADOWCLOAK) || assassinInvisibilityCheck(target)) return null;
        if (this.getCommandTags().contains("Decoy") && assassinOriginCheck(target)) return null;
        return target;
    }
    private Boolean assassinOriginCheck(LivingEntity target) {
        if (!(target instanceof PlayerEntity)) return false;
        OriginComponent component = ModComponents.ORIGIN.get(target);
        return component.getOrigins().values().stream().anyMatch(o ->
                o.getIdentifier().equals(assassinT0) ||
                o.getIdentifier().equals(assassinT1) ||
                o.getIdentifier().equals(assassinT2));
    }

    private Boolean assassinInvisibilityCheck(LivingEntity target) {
        if (!(target instanceof PlayerEntity)) return false;
        return     assassinOriginCheck(target)
                && target.hasStatusEffect(StatusEffects.INVISIBILITY)
                && target.getStatusEffect(StatusEffects.INVISIBILITY).getAmplifier() >= 4;
    }
}
