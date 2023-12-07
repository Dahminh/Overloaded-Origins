package dahminh.overloadedorigins.util;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Checks {
    private static final Identifier assassinT0 = new Identifier("assassin:upgraded0");
    private static final Identifier assassinT1 = new Identifier("assassin:upgraded1");
    private static final Identifier assassinT2 = new Identifier("assassin:upgraded2");
    public static Boolean assassinOriginCheck(LivingEntity target) {
        if (!(target instanceof PlayerEntity)) return false;
        OriginComponent component = ModComponents.ORIGIN.get(target);
        return component.getOrigins().values().stream().anyMatch(o ->
                o.getIdentifier().equals(assassinT0) ||
                        o.getIdentifier().equals(assassinT1) ||
                        o.getIdentifier().equals(assassinT2));
    }

    public static Boolean assassinInvisibilityCheck(LivingEntity target) {
        if (!(target instanceof PlayerEntity)) return false;
        return     assassinOriginCheck(target)
                && target.hasStatusEffect(StatusEffects.INVISIBILITY)
                && target.getStatusEffect(StatusEffects.INVISIBILITY).getAmplifier() >= 4;
    }
}
