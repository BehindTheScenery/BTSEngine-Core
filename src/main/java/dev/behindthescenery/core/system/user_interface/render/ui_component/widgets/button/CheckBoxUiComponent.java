package dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.button;

import dev.behindthescenery.core.system.rendering.color.RGBA;
import dev.behindthescenery.core.system.user_interface.render.MouseClick;
import net.minecraft.client.gui.DrawContext;

public class CheckBoxUiComponent extends SimpleButtonUiComponent {

    public static final RGBA UNCHECKED_COLOR = RGBA.of(0,0,0);
    public static final RGBA CHECKED_COLOR = RGBA.of(252, 163, 17);

    protected boolean checked;

    public CheckBoxUiComponent() {
        this(false);
    }

    public CheckBoxUiComponent(boolean checked) {
        this.checked = checked;
    }

    @Override
    public void onClicked(double mouseX, double mouseY, MouseClick mouseClick) {
        this.checked = !checked;
    }

    @Override
    public void refreshWidget() {

    }

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        BACKGROUND_COLOR.draw(context, x, y, w, h);

        final int s = h / 2;
        final int s2 = s /2;
        final RGBA color = checked ? CHECKED_COLOR : UNCHECKED_COLOR;

        color.draw(context, x + s2, y + s2, s, s);
    }
}
