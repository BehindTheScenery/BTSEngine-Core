package dev.behindthescenery.core.system.rendering.textures;

public record TextureParams(Runnable task) {

    public void apply() {
        task.run();
    }
}
