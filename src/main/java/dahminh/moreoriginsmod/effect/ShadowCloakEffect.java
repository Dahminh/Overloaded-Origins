package dahminh.moreoriginsmod.effect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.Random;

import static net.minecraft.particle.ParticleTypes.LARGE_SMOKE;

public class ShadowCloakEffect extends StatusEffect {

    private static final int SOUND_TICK_INTERVAL = 80;
    private static Random rand = new Random();
    protected ShadowCloakEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void applyUpdateEffect(LivingEntity e, int amplifier) {
        if (!e.world.isClient && e.age % SOUND_TICK_INTERVAL == 0) {
            e.world.playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundCategory.HOSTILE, 0.75f, 0.0f);
        }
    }

    @Override
    public void onRemoved(LivingEntity e, AttributeContainer attributes, int amplifier) {
        e.world.playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 1.0f, 2.0f);
        for (int i = 0; i<25; i++) {
            MinecraftClient.getInstance().particleManager.addParticle(
                    LARGE_SMOKE,
                    e.getX() + rand.nextGaussian() * 0.5,
                    e.getY(),
                    e.getZ() + rand.nextGaussian() * 0.5,
                    0,
                    0,
                    0);
        }
    }

    @Override
    public void onApplied(LivingEntity e, AttributeContainer attributes, int amplifier) {
        this.onRemoved(e, attributes, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
