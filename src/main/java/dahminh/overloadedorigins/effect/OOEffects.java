package dahminh.overloadedorigins.effect;

import dahminh.overloadedorigins.OverloadedOrigins;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class OOEffects {
    public static StatusEffect SHADOWBETRAYAL;
    public static StatusEffect SHADOWCLOAK;

    public static StatusEffect FEAR;

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
                        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "00397168-e904-4710-9a8b-4f539b9c133c", -1.0, EntityAttributeModifier.Operation.ADDITION)
                        .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "95e5f202-9bff-415f-81b7-420735e73c64", -0.15f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                        .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "eb68921d-ac51-4c2b-9e8b-c6bb12cc623e", -0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static void registerEffects(){
        SHADOWBETRAYAL = registerShadowBetrayalEffect("shadowbetrayal");
        SHADOWCLOAK = registerShadowCloakEffect("shadowcloak");
        FEAR = registerFearEffect("fear");
    }
}
