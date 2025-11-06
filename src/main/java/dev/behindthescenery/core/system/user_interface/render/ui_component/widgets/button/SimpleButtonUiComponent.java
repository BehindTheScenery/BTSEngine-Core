package dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.button;

import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.user_interface.render.CursorType;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import net.minecraft.client.gui.DrawContext;

public abstract class SimpleButtonUiComponent extends AbstractUiComponent {

    public static final RGBA BACKGROUND_COLOR = RGBA.of(20, 33, 61);

    @Override
    public void init() {
        setSize(100, 20);
    }

    public abstract void onClicked(double mouseX, double mouseY, MouseClick mouseClick);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, MouseClick mouseClick) {
        if(checkMouseOver(mouseX, mouseY)) {
            onClicked(mouseX, mouseY, mouseClick);
            return true;
        }

        return false;
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.HAND;
    }

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        BACKGROUND_COLOR.draw(context, x, y, w, h);
    }
}
