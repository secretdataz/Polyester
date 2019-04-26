package app.jittapan.polyester.mixin;

import app.jittapan.polyester.PolyesterMod;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(at = @At(value = "INVOKE", target="Lcom/mojang/blaze3d/platform/GlStateManager;alphaFunc(IF)V"), method="render")
    public void render(float float_1, long long_1, boolean boolean_1, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if(!mc.options.debugEnabled) {
            double scaleFactor = mc.window.getScaleFactor();
            GlStateManager.pushMatrix();
            GlStateManager.scaled(1 * scaleFactor, 1 * scaleFactor, 1 * scaleFactor);

            String toRender = PolyesterMod.INSTANCE.getFeaturesString();
            float textPosX = (mc.window.getScaledWidth() - mc.textRenderer.getStringWidth(toRender)) / 2.0f - 5.0f;

            mc.textRenderer.drawWithShadow(toRender, textPosX, 5.0f, 0xeeeeee);
            GlStateManager.popMatrix();
        }
    }
}
