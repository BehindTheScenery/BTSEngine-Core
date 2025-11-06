package dev.behindthescenery.core.mixin.user_interface;

import dev.behindthescenery.core.system.user_interface.imgui.ImGuiDrawElement;
import dev.behindthescenery.core.system.user_interface.imgui.init.ImGuiBuffers;
import dev.behindthescenery.core.system.user_interface.imgui.init.ReworkImGuiMain;
import dev.behindthescenery.core.system.user_interface.patch.IScreenPatch;
import dev.behindthescenery.core.utils.ReflectionUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(Screen.class)
public class ScreenMixin implements IScreenPatch {

    @Unique private boolean isImGuiRender$bts = false;
    @Unique private final Screen thisScreen$bts = (Screen) (Object)this;

    @Inject(method = "renderWithTooltip", at = @At("RETURN"))
    public void sdm$renderWithTooltip(DrawContext guiGraphics, int i, int j, float f, CallbackInfo ci) {
        isImGuiRender$bts = sdm$renderIMGUI(thisScreen$bts.getClass(), guiGraphics, i, j, f);
    }

    @Unique
    private boolean sdm$renderIMGUI(Class<?> cls, DrawContext guiGraphics, int i, int j, float f) {


        for (Map.Entry<Class<?>, List<ImGuiDrawElement>> classListEntry : ImGuiBuffers.getScreenRenders().entrySet()) {
            if(ReflectionUtils.canCast(cls, classListEntry.getKey())) {

                for (ImGuiDrawElement iRenderable : classListEntry.getValue()) {
                    ReworkImGuiMain.getInstance().addRender(() -> iRenderable.render(guiGraphics, i, j, f));
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean bts$isImGuiRendering() {
        return isImGuiRender$bts;
    }
}
