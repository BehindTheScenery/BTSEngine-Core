package dev.behindthescenery.core.system.rendering.vertex.patch;

import net.minecraft.client.render.BuiltBuffer;

public interface BufferBuilderPatch {

    void bts$ensureBuilding();
    void bts$endVertex();

    BuiltBuffer bts$build();

    void bts$isBuilding(boolean v);
    void bts$vertexPointer(long value);
}
