package dahminh.overloadedorigins.effect;

import dahminh.overloadedorigins.OverloadedOrigins;
import jdk.jshell.Snippet;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class OOEffects {
    public static StatusEffect SHADOW_BETRAYAL;
    public static StatusEffect SHADOW_CLOAK;
    public static StatusEffect FEAR;
    public static StatusEffect BROKEN_SHELL;

    public static StatusEffect registerShadowBetrayalEffect(String id){
        return Registry.register(Registries.STATUS_EFFECT, OverloadedOrigins.identifier(id),
            new ShadowBetrayalEffect(StatusEffectCategory.HARMFUL, 0x2A2727));
    }

    public static StatusEffect registerShadowCloakEffect(String id){
        return Registry.register(Registries.STATUS_EFFECT, OverloadedOrigins.identifier(id),
                new ShadowCloakEffect(StatusEffectCategory.BENEFICIAL, 0x000000));
    }

    public static StatusEffect registerFearEffect(String id){
        return Registry.register(Registries.STATUS_EFFECT, OverloadedOrigins.identifier(id),
                new FearEffect(StatusEffectCategory.HARMFUL, 0x36460A)
                        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "eb68921d-ac51-4c2b-9e8b-c6bb12cc623e", -1.0, EntityAttributeModifier.Operation.ADDITION)
                        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "eb68921d-ac51-4c2b-9e8b-c6bb12cc623e", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "eb68921d-ac51-4c2b-9e8b-c6bb12cc623e", -0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static StatusEffect registerBrokenShellEffect(String id){
        return Registry.register(Registries.STATUS_EFFECT, OverloadedOrigins.identifier(id),
                new BrokenShellEffect(StatusEffectCategory.NEUTRAL, 0x86708A)
                        .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "247c6f57-757e-43a8-8410-ad2b6deef89a", -0.33f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static void registerEffects(){
        SHADOW_BETRAYAL = registerShadowBetrayalEffect("shadow_betrayal");
        SHADOW_CLOAK = registerShadowCloakEffect("shadow_cloak");
        FEAR = registerFearEffect("fear");
        BROKEN_SHELL = registerBrokenShellEffect("broken_shell");
    }
}
