package dev.behindthescenery.core.system.user_interface.render.ui_component.widgets.text;

import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import dev.behindthescenery.core.system.rendering.utils.helpers.TextRenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AdvancedTextUiComponent extends SimpleTextUiComponent {

    protected int maxWight;
    protected float textScale = 1f;

    protected List<String> cachedLines = new ArrayList<>();

    public AdvancedTextUiComponent(Text text) {
        super(text);
    }

    @Override
    public AdvancedTextUiComponent setColor(SimpleColor color) {
        return (AdvancedTextUiComponent) super.setColor(color);
    }

    public AdvancedTextUiComponent setText(Text text) {
        this.text = text;
        setDirty();
        return this;
    }

    @Override
    public void setScale(float scale) {
        this.textScale = scale;
        setDirty();
    }

    @Override
    public float getScale() {
        return 1;
    }

    public void setMaxWight(int maxWight) {
        this.maxWight = maxWight;
        setDirty();
    }

    protected List<String> splitText() {
        return TextRenderHelper.splitTextToLines(text.getString(), textScale,
                maxWight == 0 ? this.getWidth() : maxWight);
    }

    protected void recreate() {
        cachedLines = splitText();
        setHeight((int) (cachedLines.size() * (MinecraftClient.getInstance().textRenderer.fontHeight / textScale)));
    }

    public void setDirty() {
        recreate();
    }

    @Override
    public void draw(DrawContext context, int x, int y, int w, int h) {
        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        final int textSize = textRenderer.fontHeight;

        final MatrixStack matrix = context.getMatrices();
        matrix.push();
        matrix.scale(textScale, textScale, textScale);

        final float scaledX = x / textScale;
        final float scaledY = y / textScale;
//        float scaledLineHeight = textSize * textScale;

        int dY = 0;
        for (String s : cachedLines) {
            context.drawText(textRenderer, s, (int) scaledX, (int) (scaledY + dY), colorI, false);
            dY += (int) textSize;
        }

        matrix.pop();
    }
}
