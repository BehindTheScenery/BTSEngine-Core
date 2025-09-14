package dev.behindthescenery.core.system.rendering.textures.atlas;

public record TextureRegion(float u0, float v0, float u1, float v1) {

    public float width() {
        return u1 - u0;
    }

    public float height() {
        return v1 - v0;
    }
}
