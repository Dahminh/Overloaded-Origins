package dahminh.overloadedorigins.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dahminh.overloadedorigins.OverloadedOrigins;
import dahminh.overloadedorigins.effect.CustomEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
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
    private static final Identifier HEARTS = new Identifier(OverloadedOrigins.MOD_ID, "textures/gui/shadow_betrayal_heart.png");

    @Final
    @Shadow private MinecraftClient client;

    @Inject(method = "drawHeart", at = @At("TAIL"), cancellable = true)
    private void drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (type == InGameHud.HeartType.CONTAINER) return;
        if (client.getCameraEntity() instanceof PlayerEntity player) {
            for (Map.Entry<StatusEffect, StatusEffectInstance> entry : player.getActiveStatusEffects().entrySet()) {
                if (entry.getKey() == CustomEffects.SHADOWBETRAYAL) {
                    RenderSystem.setShaderTexture(0, HEARTS);
                    context.drawTexture(HEARTS, x, y, halfHeart ? 9 : 0, v == 45 ? 9 : 0, 9, 9);
                    RenderSystem.setShaderTexture(0, HEARTS);
                }
            }
        }
    }
}
