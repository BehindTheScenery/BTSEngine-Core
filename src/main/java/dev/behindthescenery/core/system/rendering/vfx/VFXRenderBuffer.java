package dev.behindthescenery.core.system.rendering.vfx;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class VFXRenderBuffer {

    public static final VFXRenderBuffer INSTANCE = new VFXRenderBuffer();

    protected Map<Identifier, BufferBuilder> renderBuffer = new HashMap<>();

    public VFXRenderBuffer() {}

    public BufferBuilder getOrCreate(Identifier identifier) {
        BufferBuilder buffer = renderBuffer.get(identifier);

        if (buffer == null) {
            buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            renderBuffer.put(identifier, buffer);
        }

        return buffer;
    }

    public void draw(Identifier identifier) {
        if(renderBuffer.containsKey(identifier)) {
            RenderSystem.setShaderTexture(0, identifier);
            BuiltBuffer buf = renderBuffer.get(identifier).endNullable();
            if (buf != null) {
                BufferRenderer.drawWithGlobalProgram(buf);
                buf.close();
            }
        }
    }

    public void drawAll() {
        for (Map.Entry<Identifier, BufferBuilder> entry : renderBuffer.entrySet()) {
            RenderSystem.setShaderTexture(0, entry.getKey());
            BuiltBuffer buf = entry.getValue().endNullable();
            if (buf != null) {
                BufferRenderer.drawWithGlobalProgram(buf);
                buf.close();
            }
        }
    }

    public void startDraw() {
        renderBuffer.clear();
    }
}
