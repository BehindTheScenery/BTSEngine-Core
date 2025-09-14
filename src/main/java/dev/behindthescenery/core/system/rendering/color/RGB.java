package dev.behindthescenery.core.system.rendering.color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.utils.DrawDirection;
import dev.behindthescenery.core.system.rendering.utils.helpers.ShapesRenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import org.joml.Vector2f;

public class RGB implements SimpleColor {

    public static final RGB DEFAULT = new RGB(255,255,255);

    protected int red;
    protected int green;
    protected int blue;

    protected RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static RGB of(String hex) {
        hex = hex.replace("#", "");

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return new RGB(r, g, b);
    }

    public static RGB of(int argb) {
        return new RGB((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF);
    }

    public static RGB of(int red, int green, int blue) {
        return new RGB(red, green, blue);
    }

    public static RGB of(float red, float green, float blue) {
        return new RGB((int) (red * 255), (int) (green * 255), (int) (blue * 255));
    }

    public RGB copy() {
        return new RGB(red, green, blue);
    }

    public RGBA withAlpha(int alpha) {
        return new RGBA(red, green, blue, alpha);
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public void setRed(int red) {
        this.red = red;
    }

    @Override
    public int getRedI() {
        return red;
    }

    @Override
    public int getGreenI() {
        return green;
    }

    @Override
    public int getBlueI() {
        return blue;
    }

    @Override
    public int getAlphaI() {
        return 255;
    }

    @Override
    public float getAlphaF() {
        return 1f;
    }

    public void draw(DrawContext graphics, int x, int y, int width, int height) {
        if (width > 0 && height > 0) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);

            ShapesRenderHelper.drawQuad(graphics.getMatrices().peek().getPositionMatrix(), new Vector2f(x, y + height), new Vector2f(width, height), this);

            RenderSystem.disableBlend();
        }
    }

    public void drawCircle(DrawContext graphics, int x, int y, int radius, int segments) {
        if(radius > 0 && segments > 0) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ShapesRenderHelper.drawCircle(graphics.getMatrices().peek().getPositionMatrix(), new Vector2f(x, y), radius, segments, this);

            RenderSystem.disableBlend();
        }
    }

    public void drawTriangle(DrawContext graphics, int x, int y, int w, int h) {
        if (w > 0 && h > 0) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ShapesRenderHelper.drawTriangle(graphics.getMatrices().peek().getPositionMatrix(),new Vector2f(x,y),new Vector2f(w,h), DrawDirection.UP,this);

            RenderSystem.disableBlend();
        }
    }

    public void drawRoundFill(DrawContext guiGraphics, int x, int y, int width, int height, int radius) {
        if (width > 0 && height > 0) {
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            ShapesRenderHelper.drawRoundedRect(guiGraphics, x, y, width, height, radius, this);
            RenderSystem.disableBlend();
        }
    }
}
