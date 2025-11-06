package dev.behindthescenery.core.mixin.user_interface;

import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiBuffer;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    private void sdm$onResolutionChanged(CallbackInfo ci) {
        ReworkImGuiBuffer.resizeBuffers();
    }

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void sdm$setScreen(@Nullable Screen guiScreen, CallbackInfo ci) {
        if(guiScreen == null) return;
        ReworkImGuiBuffer.resizeBuffers();
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;endWrite()V"), method = "render")
    private void onRender(CallbackInfo ci) {
        if (ReworkImGuiMain.getInstance() != null) {
            ReworkImGuiMain.getInstance().render();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "close")
    private void onExit(CallbackInfo ci) {
        if (ReworkImGuiMain.getInstance() != null) {
            ReworkImGuiMain.getInstance().destroy();
        }
    }
}