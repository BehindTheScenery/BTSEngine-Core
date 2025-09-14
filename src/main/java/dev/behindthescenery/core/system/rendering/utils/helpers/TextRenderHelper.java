package dev.behindthescenery.core.system.rendering.utils.helpers;

import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Методы для рендера и работы с текстом
 */
public class TextRenderHelper {

    public static void drawText(DrawContext poseStack, int x, int y, float size, Text text, int textColor) {
        drawText(poseStack, MinecraftClient.getInstance().textRenderer, x, y, size, text, textColor);
    }

    public static void drawText(DrawContext poseStack, TextRenderer font, int x, int y, float size, Text text, int textColor) {
        poseStack.getMatrices().push();
        GLRenderHelper.setScale(poseStack, size, x,y);
        poseStack.drawTextWithShadow(font, text, (int) x, (int) y, textColor);
        poseStack.getMatrices().pop();
    }

    public static void drawText(DrawContext graphics, Text text, int x, int y) {
        graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text.toString(), x, y, SimpleColor.DEFAULT.argbI());
    }

    public static void drawText(DrawContext graphics, String text, int x, int y) {
        graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x, y, SimpleColor.DEFAULT.argbI());
    }

    public static void drawText(DrawContext graphics, Text text, int x, int y, SimpleColor rgb) {
        graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text.toString(), x, y, rgb.argbI());
    }

    public static void drawText(DrawContext graphics, String text, int x, int y, SimpleColor rgb) {
        graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x, y, rgb.argbI());
    }

    public static void drawTextOverWight(DrawContext graphics, String text, Vector2f pos, int wight) {
        drawTextOverWight(graphics, MinecraftClient.getInstance().textRenderer, text, pos, wight, SimpleColor.DEFAULT);
    }

    public static void drawTextOverWight(DrawContext graphics, String text, Vector2f pos, int wight, SimpleColor rgb) {
        drawTextOverWight(graphics, MinecraftClient.getInstance().textRenderer, text, pos, wight, rgb);
    }

    public static void drawTextOverWight(DrawContext graphics, TextRenderer font, String text, Vector2f pos, int wight, SimpleColor rgb) {
        if (font.getWidth(text) > wight - 10) {
            while (font.getWidth(text + "...") > wight - 10) {
                text = text.substring(0, text.length() - 1);
            }
            text += "...";
        }
        graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, (int) pos.x, (int) pos.y, rgb.argbI());
    }

    public static void drawTextInCenter(DrawContext graphics, Object text, Vector2f pos, Vector2f size, float scale, SimpleColor color) {
        int w = (int) size.x;
        int h = (int) size.y;
        int fontSize = MinecraftClient.getInstance().textRenderer.fontHeight;
        int textWidth = getTextWight(text, scale);

        float scaledFontHeight = fontSize * scale;

        float textX = pos.x + (w - textWidth) / 2.0f;
        float textY = pos.y + (h - scaledFontHeight) / 2.0f;

        graphics.getMatrices().push();
        graphics.getMatrices().scale(scale, scale, scale);


        int colorInt = color.argbI();


        if (text instanceof OrderedText) {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, (OrderedText) text, (int) (textX / scale), (int) (textY / scale), colorInt);
        } else if (text instanceof String) {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, Text.literal((String) text), (int) (textX / scale), (int) (textY / scale), colorInt);
        }

        graphics.getMatrices().pop();
    }

    public static Vector2f getTextRenderSize(Object text, int wight, float scale, int steps){
        float s = scale;
        float w = 0;

        String t = getText(text);

        for (int i = 0; i < steps; i++) {
            w = getTextWidth(t, s);
            s -= 0.01f;
            if(w <= wight)
                return new Vector2f(w,s);
        }

        return new Vector2f(s,w);
    }

    public static int getTextHeight(){
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public static float getTextHeight(float scale){
        int d1 = getTextHeight();
        int d2 = (int) (d1 * scale);
        return d1 + (d1 - d2);
    }

    public static int getTextWidth(Object text){
        return MinecraftClient.getInstance().textRenderer.getWidth(getText(text));
    }

    public static float getTextWidth(Object text, float scale) {
        return getTextWidth(text) * scale;
    }

    public static int getTextWight(Object text, float size) {
        return (int) getTextWidth(getText(text), size);
    }

    /**
     * Вычисляет разницу между изначальной шириной текста и шириной после масштабирования.
     */
    public static float getAdjustedTextWidth(Object text, float scale){
        int d1 = MinecraftClient.getInstance().textRenderer.getWidth(getText(text));
        int d2 = (int) (d1 * scale);
        int result = d1 + (d1 - d2);
        return result == d1 ? 0 : result;

    }

    /**
     * Вычисляет разницу между изначальной высотой текста и высотой после масштабирования.
     */
    public static float getAdjustedTextHeight(float scale) {
        int d1 = getTextHeight();
        int d2 = (int) (d1 * scale);
        int result = d1 + (d1 - d2);
        return result == d1 ? 0 : result;
    }

    public static List<String> splitTextToLines(String text, float textScale, int maxWidth) {
        if (text.isEmpty()) return Collections.emptyList();
        if (!text.contains(" ") && !text.contains("\n")) return Collections.singletonList(text);

        List<String> lines = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        int index = 0;
        int wordStartIndex = 0;
        boolean wordProcessing = false;
        char prevSymbol = '0';

        for (char symbol : text.toCharArray()) {
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ') {
                    wordStartIndex = index;
                }
            }

            if (symbol == '\n') {
                lines.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }

            if (getTextWidth(builder.toString() + symbol, textScale) <= maxWidth) {
                builder.append(symbol);
            } else {
                if (symbol == '.' || symbol == ',' || symbol == '!' || symbol == '?') {
                    builder.append(symbol);
                }
                if (wordProcessing) {
                    lines.add(builder.substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    lines.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ') {
                    builder.append(symbol);
                }
                index = builder.length() - 1;
            }

            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }

        if (!builder.isEmpty()) {
            lines.add(builder.toString());
        }
        return lines;
    }

    public static String getText(Object object){
        return switch (object) {
            case String str -> str;
            case Text component -> component.toString();
            case StringVisitable formattedText -> formattedText.getString();
            default -> object.toString();
        };
    }
}
