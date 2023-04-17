package dahminh.moreoriginsmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class ShadowCloakEffect extends StatusEffect {

    private static final int SOUND_TICK_INTERVAL = 80;
    protected ShadowCloakEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void applyUpdateEffect(LivingEntity e, int amplifier) {
        if (!e.world.isClient && e.age % SOUND_TICK_INTERVAL == 0) {
            e.world.playSound(null, e.getX(), e.getY(), e.getZ(), SoundEvents.ENTITY_PHANTOM_AMBIENT, SoundCategory.HOSTILE, 0.75f, 0.0f);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
