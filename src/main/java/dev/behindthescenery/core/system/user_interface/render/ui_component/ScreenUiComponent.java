package dev.behindthescenery.core.system.user_interface.render.ui_component;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.user_interface.render.ui_component.container.AbstractUiElementsContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.HashSet;

public class ScreenUiComponent extends AbstractUiElementsContainer {

    protected static Window window;
    private boolean centered = true;

    public ScreenUiComponent() {
        super(HashSet::new);
    }

    public void onInit() {

    }

    @Override
    public final void init() {
        window = MinecraftClient.getInstance().getWindow();
        setUseOffset(false);
        onInit();
    }

    public void openScreen() {
        RenderSystem.recordRenderCall(() -> {
            MinecraftClient.getInstance().setScreen(new UiScreenWrapper(this));
        });
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
        setPosX(centered ? window.getScaledWidth() / 2 - getWidth() / 2 : 0);
        setPosY(centered ? window.getScaledHeight() / 2 - getHeight() / 2 : 0);
    }

    public boolean shouldPause() {
        return true;
    }
}
