package app.jittapan.polyester.mixin;

import app.jittapan.polyester.PolyesterMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Inject(method = "handleInputEvents", at = @At("TAIL"))
    private void handleKeybinds(CallbackInfo callbackInfo) {
        PolyesterMod.INSTANCE.handleKeybinds();
        PolyesterMod.INSTANCE.assignDefaultGamma();
    }
}
