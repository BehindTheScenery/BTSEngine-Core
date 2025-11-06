package dev.behindthescenery.core.system.user_interface.render.ui_component;

import dev.behindthescenery.core.system.user_interface.render.CursorType;
import dev.behindthescenery.core.system.user_interface.render.KeyboardKey;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class UiScreenWrapper extends Screen {

    protected final ScreenUiComponent screenUiComponent;

    protected UiScreenWrapper(ScreenUiComponent screenUiComponent) {
        super(Text.empty());
        this.screenUiComponent = screenUiComponent;
    }

    @Override
    protected void init() {
        screenUiComponent.reInit();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return screenUiComponent.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) &&
                super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return screenUiComponent.mouseClicked(mouseX, mouseY, MouseClick.from(button)) &&
                super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return screenUiComponent.mouseDragged(mouseX, mouseY, MouseClick.from(button), deltaX, deltaY) &&
                super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return screenUiComponent.mouseReleased(mouseX, mouseY, MouseClick.from(button)) &&
                super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        screenUiComponent.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        boolean v1 = screenUiComponent.checkMouseOver(mouseX, mouseY);
        if(!v1) CursorType.set(null);
        return v1 && super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return screenUiComponent.keyPressed(new KeyboardKey(keyCode, scanCode, modifiers)) &&
                super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return screenUiComponent.keyReleased(new KeyboardKey(keyCode, scanCode, modifiers)) &&
                super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return screenUiComponent.charTyped(chr, modifiers) && super.charTyped(chr, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        isMouseOver(mouseX, mouseY);

        screenUiComponent.draw(context,
                screenUiComponent.getGlobalX(), screenUiComponent.getGlobalY(),
                screenUiComponent.getWidth(), screenUiComponent.getHeight()
        );
    }

    @Override
    public void tick() {
        screenUiComponent.tick();
    }

    @Override
    public boolean shouldPause() {
        return screenUiComponent.shouldPause();
    }
}
