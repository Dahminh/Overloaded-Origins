package dahminh.overloadedorigins.mixin.client;

import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.effect.OOEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {
    private static final Identifier SHADOW_BETRAYAL_HEARTS = new Identifier(OverloadedOrigins.MOD_ID, "textures/gui/sprites/hud/heart/shadow_betrayal_heart.png");

    @Final
    @Shadow private MinecraftClient client;

    @Inject(method = "drawHeart", at = @At("TAIL"), cancellable = true)
    private void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        if (type == InGameHud.HeartType.CONTAINER) return;
        if (client.getCameraEntity() instanceof PlayerEntity player) {
            for (Map.Entry<StatusEffect, StatusEffectInstance> entry : player.getActiveStatusEffects().entrySet()) {
                if (entry.getKey() == OOEffects.SHADOW_BETRAYAL) {
                    context.drawTexture(SHADOW_BETRAYAL_HEARTS, x, y, half ? 9 : 0, hardcore ? 9 : 0, 9, 9);
                }
            }
        }
    }
}
