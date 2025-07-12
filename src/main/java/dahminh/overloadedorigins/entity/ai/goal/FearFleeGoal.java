package dahminh.overloadedorigins.entity.ai.goal;

import dahminh.overloadedorigins.effect.OOEffects;
import dahminh.overloadedorigins.util.Checks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

import java.util.function.Predicate;

public class FearFleeGoal extends FleeEntityGoal<PlayerEntity> {

    private static final Predicate<LivingEntity> PHANTOM_CHECK = livingEntity -> Checks.originCheck(livingEntity, Checks.OriginsTiers.PHANTOM);
    private static final Predicate<LivingEntity> NOT_CREATIVE_OR_SPECTATOR = livingEntity -> !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
    public FearFleeGoal(PathAwareEntity mob, Class<PlayerEntity> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, livingEntity -> true, distance, slowSpeed, fastSpeed, PHANTOM_CHECK.and(NOT_CREATIVE_OR_SPECTATOR));
    }

    @Override
    public boolean canStart() {
        return this.mob.hasStatusEffect(OOEffects.FEAR) && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return this.mob.hasStatusEffect(OOEffects.FEAR) && super.shouldContinue();
    }
}
