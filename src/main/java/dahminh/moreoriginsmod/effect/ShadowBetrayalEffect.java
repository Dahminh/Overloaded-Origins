package dahminh.moreoriginsmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

public class ShadowBetrayalEffect extends StatusEffect {

    private static final int PARTICLE_TICK_INTERVAL = 5;
    private static final int SOUND_TICK_INTERVAL = 80;

    protected ShadowBetrayalEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity e, int amplifier) {
        if (e.age % PARTICLE_TICK_INTERVAL == 0) {
            e.getWorld().addParticle(
                    LARGE_SMOKE,
                    e.getX() + e.getRandom().nextGaussian() * 0.25,
                    e.getY() + e.getRandom().nextGaussian() * 0.25 + 1,
                    e.getZ() + e.getRandom().nextGaussian() * 0.25,
                    0,
                    0,
                    0);
        }
        if (!e.world.isClient && e.age % SOUND_TICK_INTERVAL == 0) {
            e.world.playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundCategory.HOSTILE, 0.75f, 0.0f);
        }
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.removeStatusEffect(StatusEffects.REGENERATION);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
