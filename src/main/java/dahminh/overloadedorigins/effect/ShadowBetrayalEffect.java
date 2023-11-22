package dahminh.overloadedorigins.effect;

import dahminh.overloadedorigins.sound.OOSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

public class ShadowBetrayalEffect extends StatusEffect {

    private static final int PARTICLE_TICK_INTERVAL = 40;
    private static final int SOUND_TICK_INTERVAL = 80;

    protected ShadowBetrayalEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity e, int amplifier) {
        if (!e.getWorld().isClient) {
            if (e.age % SOUND_TICK_INTERVAL == 0) {
                e.getWorld().playSound(null, e.getX(), e.getY(), e.getZ(), OOSounds.DARK_ELF_AMBIENT, SoundCategory.HOSTILE, 0.75f, 0.0f);
            }
            if (e.age % PARTICLE_TICK_INTERVAL == 0) {
                ((ServerWorld) e.getWorld()).spawnParticles(LARGE_SMOKE, e.getX(), e.getY() + 1, e.getZ(), 1, 0.25, 0.25, 0.25, 0);
            }
        }
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        entity.removeStatusEffect(StatusEffects.REGENERATION);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
