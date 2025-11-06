package dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.text;

import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.user_interface.render.ui_component.AbstractUiComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class SimpleTextUiComponent extends AbstractUiComponent {

    protected Text text;
    protected int colorI;

    public SimpleTextUiComponent(Text text) {
        this(text, SimpleColor.DEFAULT);
    }

    public SimpleTextUiComponent(Text text, SimpleColor color) {
        setColor(color).setText(text);
    }

    public SimpleTextUiComponent setText(Text text) {
        this.text = text;
        this.setWidth(MinecraftClient.getInstance().textRenderer.getWidth(text));
        this.setHeight(MinecraftClient.getInstance().textRenderer.fontHeight);
        return this;
    }

    public SimpleTextUiComponent setColor(SimpleColor color) {
        this.colorI = color.argbI();
        return this;
    }

    @Override
    public void init() {}

    @Override
    public void refreshWidget() {}

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, colorI, false);
    }
}
