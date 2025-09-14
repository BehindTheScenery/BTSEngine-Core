package dev.behindthescenery.core.mixin.modifie.render;

import dev.behindthescenery.core.system.rendering.vertex.patch.BufferBuilderPatch;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BufferBuilder.class)
public abstract class BufferBuilderMixin implements BufferBuilderPatch {
    @Shadow protected abstract void ensureBuilding();

    @Shadow protected abstract void endVertex();

    @Shadow @Nullable protected abstract BuiltBuffer build();

    @Shadow @Mutable private long vertexPointer;

    @Shadow @Mutable private boolean building;

    @Override
    public void bts$ensureBuilding() {
        ensureBuilding();
    }

    @Override
    public void bts$endVertex() {
        endVertex();
    }

    @Override
    public BuiltBuffer bts$build() {
        return build();
    }

    @Override
    public void bts$isBuilding(boolean v) {
        building = v;
    }

    @Override
    public void bts$vertexPointer(long value) {
        vertexPointer = value;
    }
}
