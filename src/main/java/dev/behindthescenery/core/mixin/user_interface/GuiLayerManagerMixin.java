package dev.behindthescenery.core.mixin.user_interface;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiOverlayWrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.neoforged.neoforge.client.gui.GuiLayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiLayerManager.class)
public class GuiLayerManagerMixin {

    @Inject(method = "renderInner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    public void bts$renderInner(DrawContext guiGraphics, RenderTickCounter partialTick, CallbackInfo ci) {
        guiGraphics.getMatrices().translate(0,0, GuiLayerManager.Z_SEPARATION);
        ImGuiOverlayWrapper.renderAll(guiGraphics, 0,0,0);
    }
}
