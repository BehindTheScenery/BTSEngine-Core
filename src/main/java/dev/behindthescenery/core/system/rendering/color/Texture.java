package dev.behindthescenery.core.system.rendering.color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.behindthescenery.core.system.rendering.BtsRenderSystem;
import dev.behindthescenery.core.system.rendering.utils.helpers.TextureRenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;

public class Texture implements SimpleColor{

    public static final Codec<Texture> CODEC = RecordCodecBuilder.create(textureInstance ->
            textureInstance.group(
                    Identifier.CODEC.fieldOf("textureId").forGetter(Texture::getTextureID),
                    Codec.INT.fieldOf("red").forGetter(Texture::getRedI),
                    Codec.INT.fieldOf("green").forGetter(Texture::getGreenI),
                    Codec.INT.fieldOf("blue").forGetter(Texture::getBlueI),
                    Codec.INT.fieldOf("alpha").forGetter(Texture::getAlphaI),
                    Codec.DOUBLE.fieldOf("tileSize").forGetter(Texture::getTileSize),
                    Codec.FLOAT.fieldOf("minU").forGetter(Texture::getMinU),
                    Codec.FLOAT.fieldOf("minV").forGetter(Texture::getMinV),
                    Codec.FLOAT.fieldOf("maxU").forGetter(Texture::getMaxU),
                    Codec.FLOAT.fieldOf("maxV").forGetter(Texture::getMinV)
            ).apply(textureInstance, Texture::new));

    protected final Identifier textureID;

    protected double tileSize;

    protected float minU;
    protected float minV;
    protected float maxU;
    protected float maxV;

    protected int red;
    protected int green;
    protected int blue;
    protected int alpha;

    public Texture(Identifier textureID) {
        this(textureID, 255, 255, 255, 255);
    }

    public Texture(Identifier textureID, int r, int g, int b, int a) {
        this.textureID = textureID;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        this.tileSize = 0;
    }

    public Texture(Identifier textureID, int r, int g, int b, int a, double tileSize, float minU, float minV, float maxU, float maxV) {
        this.textureID = textureID;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        this.tileSize = tileSize;
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
    }

    @OnlyIn(Dist.CLIENT)
    public static Texture createWithGlParam(Identifier textureID, Runnable task) {
        Texture texture = new Texture(textureID);
        BtsRenderSystem.exec(() -> {;
            GL15.glBindTexture(GL15.GL_TEXTURE_2D, texture.getTexture().getGlId());
            task.run();
            GL15.glBindTexture(GL15.GL_TEXTURE_2D, 0);
        });
        return texture;
    }

    public Texture withAlpha(int a) {
        this.alpha = a;
        return this;
    }

    public Texture withColor(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        return this;
    }

    public Texture withUv(float minU, float minV, float maxU, float maxV) {
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
        return this;
    }

    public Texture withTileSize(final double tileSize) {
        this.tileSize = tileSize;
        return this;
    }

    public Texture copy() {
        return new Texture(textureID, red, green, blue, alpha).withUv(minU, minV, maxU, maxV).withTileSize(tileSize);
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
        return alpha;
    }

    public float getMinU() {
        return minU;
    }

    public float getMinV() {
        return minV;
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }

    public double getTileSize() {
        return tileSize;
    }

    public Identifier getTextureID() {
        return textureID;
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractTexture getTexture() {
        final TextureManager manager = MinecraftClient.getInstance().getTextureManager();
        AbstractTexture tex = manager.getTexture(this.textureID);
        if (tex == null) {
            tex = new ResourceTexture(this.textureID);
            manager.registerTexture(this.textureID, tex);
        }
        return tex;
    }

    @OnlyIn(Dist.CLIENT)
    public void setupTexture() {
        RenderSystem.setShaderTexture(0, getTexture().getGlId());
    }

    @OnlyIn(Dist.CLIENT)
    public void draw(final DrawContext graphics, final int x, final int y, final int w, final int h) {
        this.setupTexture();
        if (tileSize <= 0) {
            TextureRenderHelper.renderTextureRect(graphics, x, y, w, h, this, this.minU, this.minV, this.maxU, this.maxV);
        } else {
            final int r = this.red;
            final int g = this.green;
            final int b = this.blue;
            final int a = this.alpha;
            final Matrix4f m = graphics.getMatrices().peek().getPositionMatrix();
            final Tessellator tesselator = Tessellator.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            final BufferBuilder buffer = tesselator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            buffer.vertex(m, (float) x, (float) (y + h), 0.0F).color(r, g, b, a).texture((float) ((double) x / this.tileSize), (float) ((double) (y + h) / this.tileSize));
            buffer.vertex(m, (float) (x + w), (float) (y + h), 0.0F).color(r, g, b, a).texture((float) ((double) (x + w) / this.tileSize), (float) ((double) (y + h) / this.tileSize));
            buffer.vertex(m, (float) (x + w), (float) y, 0.0F).color(r, g, b, a).texture((float) ((double) (x + w) / this.tileSize), (float) ((double) y / this.tileSize));
            buffer.vertex(m, (float) x, (float) y, 0.0F).color(r, g, b, a).texture((float) ((double) x / this.tileSize), (float) ((double) y / this.tileSize));
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.disableBlend();
        }
    }
}
