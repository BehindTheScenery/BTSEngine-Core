package dev.behindthescenery.core.system.rendering.utils.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.buffers.RenderBuffer2D;
import dev.behindthescenery.core.system.rendering.color.SimpleColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Методы для рендера и работы с Текстурами
 */
public class TextureRenderHelper {

    public static void renderTexture(DrawContext guiGraphics, String texture, int x, int y, int width, int height, int textureX, int textureY, int textureW, int textureH){
        renderTexture(guiGraphics, texture, x,y,width,height, textureX, textureY, textureW, textureH, 256);
    }

    public static void renderTexture(DrawContext guiGraphics, String texture, int x, int y, int width, int height, int textureX, int textureY, int textureW, int textureH, int textureSize){
        guiGraphics.drawTexture(Identifier.of(texture), x,y,width,height, textureX, textureY, textureW, textureH, textureSize, textureSize );
    }

    public static void renderTexture(DrawContext guiGraphics, String texture, int x, int y, int width, int height, int textureX, int textureY, int textureW, int textureH, int textureSizeX, int textureSizeY){
        guiGraphics.drawTexture(Identifier.of(texture), x,y,width,height, textureX, textureY, textureW, textureH, textureSizeX, textureSizeY );
    }

    public static void renderTexture(DrawContext guiGraphics, Identifier texture, int x, int y, int width, int height, int textureX, int textureY, int textureW){
        guiGraphics.drawTexture(texture, x,y,width,height, textureX, textureY, textureW, 256, 256);
    }

    public static void renderTexture(DrawContext guiGraphics, Identifier texture, int x, int y, int width, int height, int textureX, int textureY, int textureW, int textureH, int textureSize){
        guiGraphics.drawTexture(texture, x,y,width,height, textureX, textureY, textureW, textureH, textureSize);
    }

    public static void renderTexture(DrawContext guiGraphics, Identifier texture, int x, int y, int width, int height, int textureX, int textureY, int textureW, int textureH, int textureSizeX, int textureSizeY){
        guiGraphics.drawTexture(texture, x,y,width,height, textureX, textureY, textureW, textureH, textureSizeX, textureSizeY );
    }

    public static void renderTextureRect(DrawContext graphics, int x, int y, int w, int h, SimpleColor col, float u0, float v0, float u1, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_TEXTURE_COLOR);
        RenderSystem.enableBlend();
        RenderBuffer2D.addRectToBufferWithUV(graphics.getMatrices().peek().getPositionMatrix(), buffer, x, y, w, h, col, u0, v0, u1, v1);

        BtsRenderSystem.drawWithGlobalShader(buffer.end());
        RenderSystem.disableBlend();
    }

    public static void renderSlicedTextureRect(DrawContext guiGraphics, int x, int y, int width, int height, int sliceSize, int textureW, int textureH) {
        int rightX = x + width - sliceSize;
        int bottomY = y + height - sliceSize;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Левый верхний угол
        blit(guiGraphics, x, y, sliceSize, sliceSize, 0, 0, sliceSize, sliceSize, textureW, textureH);
        // Правый верхний угол
        blit(guiGraphics, rightX, y, sliceSize, sliceSize, textureW - sliceSize, 0, sliceSize, sliceSize, textureW, textureH);
        // Левый нижний угол
        blit(guiGraphics, x, bottomY, sliceSize, sliceSize, 0, textureH - sliceSize, sliceSize, sliceSize, textureW, textureH);
        // Правый нижний угол
        blit(guiGraphics, rightX, bottomY, sliceSize, sliceSize, textureW - sliceSize, textureH - sliceSize, sliceSize, sliceSize, textureW, textureH);

        // Верхняя сторона
        blit(guiGraphics, x + sliceSize, y, width - sliceSize * 2, sliceSize, sliceSize, 0, textureW - sliceSize * 2, sliceSize, textureW, textureH);
        // Нижняя сторона
        blit(guiGraphics, x + sliceSize, bottomY, width - sliceSize * 2, sliceSize, sliceSize, textureH - sliceSize, textureW - sliceSize * 2, sliceSize, textureW, textureH);
        // Левая сторона
        blit(guiGraphics, x, y + sliceSize, sliceSize, height - sliceSize * 2, 0, sliceSize, sliceSize, textureH - sliceSize * 2, textureW, textureH);
        // Правая сторона
        blit(guiGraphics, rightX, y + sliceSize, sliceSize, height - sliceSize * 2, textureW - sliceSize, sliceSize, sliceSize, textureH - sliceSize * 2, textureW, textureH);

        // Центральная часть
        blit(guiGraphics, x + sliceSize, y + sliceSize, width - sliceSize * 2, height - sliceSize * 2, sliceSize, sliceSize, textureW - sliceSize * 2, textureH - sliceSize * 2, textureW, textureH);
        RenderSystem.disableBlend();
    }


    public static void renderSlicedTexture(DrawContext guiGraphics, Identifier texture, int x, int y, int width, int height, int sliceSize, int textureW, int textureH) {
        int rightX = x + width - sliceSize;
        int bottomY = y + height - sliceSize;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Левый верхний угол
        guiGraphics.drawTexture(texture, x, y, sliceSize, sliceSize, 0, 0, sliceSize, sliceSize, textureW, textureH);
        // Правый верхний угол
        guiGraphics.drawTexture(texture, rightX, y, sliceSize, sliceSize, textureW - sliceSize, 0, sliceSize, sliceSize, textureW, textureH);
        // Левый нижний угол
        guiGraphics.drawTexture(texture, x, bottomY, sliceSize, sliceSize, 0, textureH - sliceSize, sliceSize, sliceSize, textureW, textureH);
        // Правый нижний угол
        guiGraphics.drawTexture(texture, rightX, bottomY, sliceSize, sliceSize, textureW - sliceSize, textureH - sliceSize, sliceSize, sliceSize, textureW, textureH);

        // Верхняя сторона
        guiGraphics.drawTexture(texture, x + sliceSize, y, width - sliceSize * 2, sliceSize, sliceSize, 0, textureW - sliceSize * 2, sliceSize, textureW, textureH);
        // Нижняя сторона
        guiGraphics.drawTexture(texture, x + sliceSize, bottomY, width - sliceSize * 2, sliceSize, sliceSize, textureH - sliceSize, textureW - sliceSize * 2, sliceSize, textureW, textureH);
        // Левая сторона
        guiGraphics.drawTexture(texture, x, y + sliceSize, sliceSize, height - sliceSize * 2, 0, sliceSize, sliceSize, textureH - sliceSize * 2, textureW, textureH);
        // Правая сторона
        guiGraphics.drawTexture(texture, rightX, y + sliceSize, sliceSize, height - sliceSize * 2, textureW - sliceSize, sliceSize, sliceSize, textureH - sliceSize * 2, textureW, textureH);

        // Центральная часть
        guiGraphics.drawTexture(texture, x + sliceSize, y + sliceSize, width - sliceSize * 2, height - sliceSize * 2, sliceSize, sliceSize, textureW - sliceSize * 2, textureH - sliceSize * 2, textureW, textureH);
        RenderSystem.disableBlend();
    }


    /**
     * @return x = width, y = height
     */
    public static Vector2f getTextureSize(Identifier texture){
        MinecraftClient minecraft = MinecraftClient.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(texture);

        if (abstractTexture != null) {
            NativeImage nativeImage = (abstractTexture instanceof NativeImageBackedTexture) ? ((NativeImageBackedTexture) abstractTexture).getImage() : null;

            if (nativeImage != null) {
                int textureWidth = nativeImage.getWidth();
                int textureHeight = nativeImage.getHeight();

                return new Vector2f(textureWidth, textureHeight);
            }
        }

        return new Vector2f(0,0);
    }


    public static void blit(DrawContext guiGraphics, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        blit(guiGraphics, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    public static void blit(DrawContext guiGraphics, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        blit(guiGraphics, x, y, width, height, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }

    private static void blit(DrawContext guiGraphics, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        innerBlit(guiGraphics, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float)textureWidth, (vOffset + 0.0F) / (float)textureHeight, (vOffset + (float)vHeight) / (float)textureHeight);
    }

    private static void innerBlit(DrawContext guiGraphics, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = guiGraphics.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferbuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).texture(minU, minV);
        bufferbuilder.vertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).texture(minU, maxV);
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).texture(maxU, maxV);
        bufferbuilder.vertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).texture(maxU, minV);
        BtsRenderSystem.drawWithGlobalShader(bufferbuilder.end());

    }
}

