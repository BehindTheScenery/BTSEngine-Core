package dev.behindthescenery.core.system.rendering.color;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TextureAtlas extends Texture{

    public TextureAtlas(Identifier textureID) {
        super(textureID);
    }

    public TextureAtlas(Identifier textureID, int r, int g, int b, int a) {
        super(textureID, r, g, b, a);
    }

    @Override
    public Texture copy() {
        return new TextureAtlas(textureID, red, green, blue, alpha).withUv(minU, minV, maxU, maxV).withTileSize(tileSize);
    }

    @Override
    public void draw(DrawContext graphics, int x, int y, int w, int h) {
        Sprite sprite = MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(this.textureID);
        if (sprite != null) {
            Matrix4f m = graphics.getMatrices().peek().getPositionMatrix();
            int r = this.red;
            int g = this.green;
            int b = this.blue;
            int a = this.alpha;
            float minU = sprite.getMinU();
            float minV = sprite.getMinV();
            float maxU = sprite.getMaxU();
            float maxV = sprite.getMaxV();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, sprite.getAtlasId());
            BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            buffer.vertex(m, (float)x, (float)y, 0.0F).color(r, g, b, a).texture(minU, minV);
            buffer.vertex(m, (float)x, (float)(y + h), 0.0F).color(r, g, b, a).texture(minU, maxV);
            buffer.vertex(m, (float)(x + w), (float)(y + h), 0.0F).color(r, g, b, a).texture(maxU, maxV);
            buffer.vertex(m, (float)(x + w), (float)y, 0.0F).color(r, g, b, a).texture(maxU, minV);
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.disableBlend();
        }
    }
}
