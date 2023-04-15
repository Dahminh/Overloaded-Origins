package dahminh.moreoriginsmod.effect;

import dahminh.moreoriginsmod.MoreOriginsMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomEffects {
    public static StatusEffect SHADOWBETRAYAL;

    public static StatusEffect registerShadowBetrayalEffect(String id){
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(MoreOriginsMod.MOD_ID, id),
            new ShadowBetrayalEffect(StatusEffectCategory.HARMFUL, 0x2A2727));
    }

    public static void registerEffects(){
        SHADOWBETRAYAL = registerShadowBetrayalEffect("shadowbetrayal");
    }
}
