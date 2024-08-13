package dahminh.overloadedorigins.util;

import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Checks {

    public static enum OriginsTiers {
        ASSASSIN(new Identifier("assassin:upgraded0"), new Identifier("assassin:upgraded1"), new Identifier("assassin:upgraded2")),
        PHANTOM(new Identifier("phantom:upgraded0"), new Identifier("phantom:upgraded1"), new Identifier("phantom:upgraded2"));

        private Identifier tierOne;
        private Identifier tierTwo;
        private Identifier tierThree;
        OriginsTiers(Identifier tierOne, Identifier tierTwo, Identifier tierThree) {
            this.tierOne   = tierOne;
            this.tierTwo   = tierTwo;
            this.tierThree = tierThree;
        }

        public boolean checkOriginIdentifier(Identifier input) {
            return input.equals(this.tierOne) || input.equals(this.tierTwo) || input.equals(this.tierThree);
        }

    }

    public static Boolean originCheck(LivingEntity target, OriginsTiers origin) {
        if (!(target instanceof PlayerEntity)) return false;
        OriginComponent component = ModComponents.ORIGIN.get(target);
        return component.getOrigins().values().stream().anyMatch(o -> origin.checkOriginIdentifier(o.getIdentifier()));
    }

    public static Boolean assassinInvisibilityCheck(LivingEntity target) {
        if (!(target instanceof PlayerEntity)) return false;
        return     originCheck(target, OriginsTiers.ASSASSIN)
                && target.hasStatusEffect(StatusEffects.INVISIBILITY)
                && target.getStatusEffect(StatusEffects.INVISIBILITY).getAmplifier() >= 4;
    }
}
