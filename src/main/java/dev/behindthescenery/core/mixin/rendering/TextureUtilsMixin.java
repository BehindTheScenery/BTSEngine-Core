package dev.behindthescenery.core.mixin.rendering;

import com.mojang.blaze3d.platform.TextureUtil;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureUtil.class)
public class TextureUtilsMixin {

    @Inject(method = "prepareImage(Lnet/minecraft/client/texture/NativeImage$InternalFormat;IIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/TextureUtil;bind(I)V", shift = At.Shift.AFTER))
    private static void bts$prepareImageAndHook(NativeImage.InternalFormat internalFormat, int id, int maxLevel, int width, int height, CallbackInfo ci) {
        BtsRenderSystem.applyTextureParams();
    }
}
