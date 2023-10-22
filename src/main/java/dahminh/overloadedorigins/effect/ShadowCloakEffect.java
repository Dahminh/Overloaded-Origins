package dahminh.overloadedorigins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

public class ShadowCloakEffect extends StatusEffect {

    private static final int SOUND_TICK_INTERVAL = 80;

    protected ShadowCloakEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void applyUpdateEffect(LivingEntity e, int amplifier) {
        if (!e.getWorld().isClient && e.age % SOUND_TICK_INTERVAL == 0) {
            e.getWorld().playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundCategory.HOSTILE, 0.75f, 0.0f);
        }
    }
    @Override
    public void onApplied(LivingEntity e, int amplifier) {
        if (e.getWorld().isClient) {
            return;
        }
        e.getWorld().playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 1.0f, 2.0f);
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

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
