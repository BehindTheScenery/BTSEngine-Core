package dev.behindthescenery.core.system.rendering.vertex;

import net.minecraft.client.render.VertexConsumer;

public class BtsVertexConsumer<T extends BtsUvExpander> implements VertexConsumer {

    private final VertexConsumer delegate;
    private final T sprite;

    public BtsVertexConsumer(VertexConsumer delegate, T sprite) {
        this.delegate = delegate;
        this.sprite = sprite;
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z) {
        this.delegate.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        this.delegate.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        this.delegate.texture(this.sprite.getFrameU(u), this.sprite.getFrameV(v));
        return this;
    }

    @Override
    public VertexConsumer overlay(int u, int v) {
        this.delegate.overlay(u, v);
        return this;
    }

    @Override
    public VertexConsumer light(int u, int v) {
        this.delegate.light(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        this.delegate.normal(x, y, z);
        return this;
    }

    @Override
    public void vertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
        this.delegate.vertex(x, y, z, color, this.sprite.getFrameU(u), this.sprite.getFrameV(v),
                packedOverlay, packedLight, normalX, normalY, normalZ);
    }

    public VertexConsumer getDelegate() {
        return delegate;
    }
}
